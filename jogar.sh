#!/bin/sh
# Executa Dun Nahar (precisa de Java 17+)
cd "$(dirname "$0")"
exec java -Dfile.encoding=UTF-8 -jar dunnahar.jar
