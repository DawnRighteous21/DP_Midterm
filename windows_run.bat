@echo off
chcp 65001 > nul
echo =^> Dang tim kiem ma nguon...

echo =^> Dang bien dich du an...
if not exist bin mkdir bin
javac -d bin src/model/*.java src/extraction/*.java src/validation/*.java src/notification/*.java src/storage/*.java src/service/*.java src/ui/*.java

if %ERRORLEVEL% equ 0 (
    echo =^> Bien dich thanh cong! Dang khoi chay ung dung...
    java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp bin ui.MainSwingUI
) else (
    echo =^> Bien dich that bai. Vui long kiem tra loi phia tren.
)

echo =^> Dang don dep file .class...
rmdir /s /q bin

echo =^> Hoan tat!
pause
