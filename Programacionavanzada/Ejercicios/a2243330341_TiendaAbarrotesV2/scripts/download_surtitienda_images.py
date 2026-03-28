import json
import os
import re
from html import unescape
from urllib.parse import quote, urljoin, urlparse
from urllib.request import Request, urlopen


BASE_URL = "https://www.surtitienda.mx/"
WORKDIR = os.path.dirname(os.path.dirname(__file__))
INVENTARIO_PATH = os.path.join(WORKDIR, "inventario.json")
CATALOGO_URLS_PATH = os.path.join(WORKDIR, "catalogo_urls.json")
IMG_DIR = os.path.join(WORKDIR, "src", "img")
REPORT_PATH = os.path.join(WORKDIR, "reporte_descarga_imagenes.txt")

USER_AGENT = "Mozilla/5.0 (compatible; TiendaAbarrotesBot/1.0)"


def fetch_text(url, timeout=20):
    url = safe_url(url)
    req = Request(url, headers={"User-Agent": USER_AGENT})
    with urlopen(req, timeout=timeout) as resp:
        ctype = resp.headers.get("Content-Type", "")
        data = resp.read()
    if ("text/html" not in ctype and "application/json" not in ctype
            and "xml" not in ctype and not url.endswith("/p")):
        return ""
    return data.decode("utf-8", errors="ignore")


def fetch_bytes(url, timeout=25):
    url = safe_url(url)
    req = Request(url, headers={"User-Agent": USER_AGENT})
    with urlopen(req, timeout=timeout) as resp:
        return resp.read()


def same_host(url):
    return urlparse(url).netloc.endswith("surtitienda.mx")


def normalize_url(url):
    if not url:
        return None
    url = url.split("#")[0]
    if url.startswith("//"):
        url = "https:" + url
    if url.startswith("/"):
        url = urljoin(BASE_URL, url)
    if not url.startswith("http"):
        return None
    return url


def safe_url(url):
    """Ensure URL path/query is ASCII-safe for urllib Request."""
    p = urlparse(url)
    path = quote(p.path, safe="/:%._-~")
    query = quote(p.query, safe="=&:%._-~")
    rebuilt = f"{p.scheme}://{p.netloc}{path}"
    if query:
        rebuilt += "?" + query
    return rebuilt


def get_product_urls_from_sitemap():
    sitemap_index = fetch_text(urljoin(BASE_URL, "sitemap.xml"))
    sitemap_files = re.findall(r"https://www\.surtitienda\.mx/sitemap/product-\d+\.xml", sitemap_index)
    urls = set()
    for sm in sitemap_files:
        xml = fetch_text(sm)
        urls.update(re.findall(r"<loc>(https://www\.surtitienda\.mx/[^<]+/p)</loc>", xml))
    return sorted(urls)


def extract_og_image(html):
    m = re.search(
        r'<meta[^>]*property=["\']og:image["\'][^>]*content=["\']([^"\']+)["\']',
        html,
        re.IGNORECASE,
    )
    if m:
        return normalize_url(unescape(m.group(1).strip()))
    m2 = re.search(r'"image"\s*:\s*"([^"]+)"', html, re.IGNORECASE)
    if m2:
        return normalize_url(unescape(m2.group(1).replace("\\/", "/")))
    return None


def main():
    os.makedirs(IMG_DIR, exist_ok=True)
    with open(INVENTARIO_PATH, "r", encoding="utf-8") as f:
        inventario = json.load(f)
    skus = [p["sku"] for p in inventario]

    print(f"Inventario SKUs: {len(skus)}")
    product_urls = get_product_urls_from_sitemap()
    print(f"Product URLs encontradas: {len(product_urls)}")
    if not product_urls:
        raise SystemExit("No se encontraron URLs de producto.")

    # fill mapping SKU->product URL cycling if fewer URLs
    sku_to_product = {}
    for i, sku in enumerate(skus):
        sku_to_product[sku] = product_urls[i % len(product_urls)]

    downloaded = 0
    failed = 0
    lines = []

    for sku in skus:
        purl = sku_to_product[sku]
        img_path = os.path.join(IMG_DIR, f"{sku}.jpg")
        try:
            html = fetch_text(purl)
            img_url = extract_og_image(html)
            if not img_url:
                raise RuntimeError("No og:image")
            data = fetch_bytes(img_url)
            if not data:
                raise RuntimeError("Imagen vacía")
            with open(img_path, "wb") as f:
                f.write(data)
            downloaded += 1
            lines.append(f"OK   {sku} <- {purl} | {img_url}")
        except Exception as e:
            failed += 1
            lines.append(f"FAIL {sku} <- {purl} | {e}")

    with open(CATALOGO_URLS_PATH, "w", encoding="utf-8") as f:
        json.dump(sku_to_product, f, ensure_ascii=False, indent=2)
    with open(REPORT_PATH, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))

    print(f"Descargadas: {downloaded}")
    print(f"Fallidas: {failed}")
    print(f"Imagenes en: {IMG_DIR}")
    print(f"catalogo_urls.json actualizado: {CATALOGO_URLS_PATH}")
    print(f"Reporte: {REPORT_PATH}")


if __name__ == "__main__":
    main()

