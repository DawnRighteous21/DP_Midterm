@echo off
chcp 65001 > nul
echo =^> Dang tim kiem ma nguon...
dir /s /B src\*.java > sources.txt

echo =^> Dang bien dich du an...
if not exist bin mkdir bin
javac -d bin @sources.txt

if %ERRORLEVEL% equ 0 (
    echo =^> Bien dich thanh cong! Dang khoi chay ung dung...
    java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp bin ui.MainSwingUI
) else (
    echo =^> Bien dich that bai. Vui long kiem tra loi phia tren.
)

echo =^> Dang don dep file .class...
rmdir /s /q bin
del sources.txt

echo =^> Hoan tat!
pause
