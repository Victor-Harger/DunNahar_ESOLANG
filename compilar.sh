#!/bin/sh
# Recompila o projeto e regenera o dunnahar.jar (precisa do JDK 17+)
cd "$(dirname "$0")"
rm -rf out && mkdir out
javac --release 17 -encoding UTF-8 -d out src/dunnahar/*.java || exit 1
printf 'Main-Class: dunnahar.Main\n' > manifest.txt
jar cfm dunnahar.jar manifest.txt -C out . && rm manifest.txt
echo "dunnahar.jar gerado."
