#!/bin/bash

echo "=> Đang tìm kiếm mã nguồn..."
find src -name "*.java" > sources.txt

echo "=> Đang biên dịch dự án..."
mkdir -p bin
javac -d bin @sources.txt

# Kiểm tra xem quá trình biên dịch có thành công không (Mã lỗi 0)
if [ $? -eq 0 ]; then
    echo "=> Biên dịch thành công! Đang khởi chạy ứng dụng..."

    # Ép biến môi trường sửa lỗi màn hình trắng cho Wayland/Niri (CachyOS)
    export _JAVA_AWT_WM_NONREPARENTING=1

    # Chạy ứng dụng từ thư mục bin với cờ khử răng cưa Font
    java -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -cp bin ui.MainSwingUI
else
    echo "=> Biên dịch thất bại. Vui lòng kiểm tra lỗi phía trên."
fi

echo "=> Đang dọn dẹp file .class..."
rm -rf bin
rm -f sources.txt

echo "=> Hoàn tất!"
