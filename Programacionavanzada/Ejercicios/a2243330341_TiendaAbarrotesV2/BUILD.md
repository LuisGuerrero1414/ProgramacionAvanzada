# Compilar y ejecutar (línea de comandos)

Las clases compiladas van a la carpeta `bin`. La dependencia **Gson** está en el proyecto: `lib/gson-2.13.2.jar`.

El proyecto está pensado para **Java 21** (mismo nivel que Eclipse en `agent.md`). Usa **`javac --release 21`** para generar bytecode **65.0** compatible con el JRE 21. Si compilas sin `--release` con un JDK más nuevo (p. ej. 24), las clases pueden ser **68.0** y Eclipse fallará con `UnsupportedClassVersionError` al ejecutar.

Desde la raíz del proyecto (`a2243330341_TiendaAbarrotes`), en **PowerShell**:

```powershell
$gson = "lib\gson-2.13.2.jar"
if (-not (Test-Path bin)) { New-Item -ItemType Directory -Path bin | Out-Null }
javac --release 21 -d bin -encoding UTF-8 -cp $gson (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp "bin;$gson" Main
```

En **CMD** (símbolo del sistema):

```cmd
set GSON=lib\gson-2.13.2.jar
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac --release 21 -d bin -encoding UTF-8 -cp %GSON% @sources.txt
del sources.txt
java -cp bin;%GSON% Main
```

Eclipse usa la misma librería mediante `.classpath` (`lib/gson-2.13.2.jar`).
