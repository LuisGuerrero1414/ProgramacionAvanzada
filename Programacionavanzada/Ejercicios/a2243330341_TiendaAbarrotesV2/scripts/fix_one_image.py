import re
import urllib.request

sku = "7501000000187"
product_url = "https://www.surtitienda.mx/cerveza-victoria-lata-473ml-victoria-17501064194136/p"
dest = r"c:\Users\ferna\eclipse-workspace\a2243330341_TiendaAbarrotesV2\src\img\\" + sku + ".jpg"

html = urllib.request.urlopen(product_url, timeout=25).read().decode("utf-8", "ignore")
m = re.search(r'<meta[^>]*property=["\']og:image["\'][^>]*content=["\']([^"\']+)["\']', html, re.I)
if not m:
    raise RuntimeError("og:image no encontrado")
img_url = m.group(1)
data = urllib.request.urlopen(img_url, timeout=25).read()
with open(dest, "wb") as f:
    f.write(data)
print("ok", sku, len(data))
