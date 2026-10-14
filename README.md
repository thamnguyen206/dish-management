# Quản lý món ăn - Đề giữa kỳ (Spring Boot + Thymeleaf + JPA/Hibernate + SQL Server)

MSSV: **BIT240269**

## Chạy project
1. SSMS: mở và chạy toàn bộ file `BIT240269_MID.sql` (tạo database `BIT240269_MID`, 3 bảng, 3 loại + 6 món mẫu).
2. Kiểm tra `src/main/resources/application.properties` (tên server `LAPTOP-12OGD3V1`, Windows Authentication).
3. JDK 17+, mở project bằng VS Code, bấm Run trên hàm `main` của `DishManagementApplication`
   (hoặc `mvn spring-boot:run`), mở http://localhost:8080

## Đối chiếu đề
| Yêu cầu | Nằm ở đâu |
|---|---|
| Tiền tố MSSV: database, 3 bảng, mọi cột | `BIT240269_MID.sql` + `@Table/@Column` trong `entity/` |
| Model có MSSV | `Dish_BIT240269`, `DishCategory_BIT240269`, `DishImage_BIT240269` |
| Giữ đúng tên cột `BIT240269_...` | `spring.jpa.hibernate.naming.physical-strategy=...PhysicalNamingStrategyStandardImpl` |
| Validation, tên món không trùng trong loại, DishCategoryId tồn tại | `DishForm`, `DishService.validateBusiness` |
| Tìm / lọc loại / lọc trạng thái / khoảng giá / sắp xếp, lọc ở tầng DB | `DishSpecs` (Criteria API -> WHERE / ORDER BY) |
| Giữ điều kiện lọc sau khi gửi form | `dish/index.html` (th:value / th:selected từ `filter`) |
| Quick (<= 15 phút) / Regular (> 15), không có cột DB | `Dish_BIT240269.getTimeTypeKey()` |
| Thêm ảnh bằng URL, nhiều ảnh, đổi ảnh đại diện (tối đa 1) | `DishService.addImage / setThumbnail`, `DishImageRepository.clearThumbnail`, chỉ mục unique trong SQL |
| Không xóa loại đang có món | `DishCategoryService.delete` (+ khóa ngoại trong SQL) |
| Món ngừng bán vẫn hiện ở trang quản trị; lọc "Available" thì ẩn | `DishSpecs` (status) |
| Lỗi Id không tồn tại, khoảng giá sai, món chưa có ảnh, không có kết quả | `GlobalExceptionHandler`, `DishController.index`, `dish/*.html` |
| Đa ngôn ngữ EN (mặc định) <-> VI | `WebConfig`, `LangUrlHelper`, `messages.properties`, `messages_vi.properties` |

## Kịch bản demo (theo mục 5 của đề)
1. SSMS: cho thấy `BIT240269_MID`, 3 bảng, cột có tiền tố; VS Code: tên 3 Model có MSSV.
2. Danh sách món + loại món: tìm theo tên, lọc loại, lọc "Available", khoảng giá, sắp xếp; bấm tìm xong điều kiện vẫn còn.
3. Cột Type (Quick/Regular); thêm hoặc sửa một món (thử để trống tên, giá = 0 để thấy validation).
4. Vào Detail: thêm ảnh bằng URL, thêm nhiều ảnh, "Set as thumbnail" để đổi ảnh đại diện.
5. Trang Categories: xóa loại đang có món -> báo lỗi; gõ http://localhost:8080/dishes/9999 -> trang "không tìm thấy".
6. Mở web lần đầu: English -> bấm "Tiếng Việt" -> kiểm tra menu, nhãn, nút, validation, Quick/Regular; đi qua List/Create/Edit/Detail ngôn ngữ vẫn giữ; ở List đang lọc thì đổi ngôn ngữ điều kiện lọc không mất.
