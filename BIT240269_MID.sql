
USE master;
GO
IF DB_ID(N'BIT240269_MID') IS NULL
    CREATE DATABASE BIT240269_MID;
GO
USE BIT240269_MID;
GO


IF OBJECT_ID(N'dbo.BIT240269_DishImages', N'U')     IS NOT NULL DROP TABLE dbo.BIT240269_DishImages;
IF OBJECT_ID(N'dbo.BIT240269_Dishes', N'U')         IS NOT NULL DROP TABLE dbo.BIT240269_Dishes;
IF OBJECT_ID(N'dbo.BIT240269_DishCategories', N'U') IS NOT NULL DROP TABLE dbo.BIT240269_DishCategories;
GO


CREATE TABLE dbo.BIT240269_DishCategories (
    BIT240269_Id          BIGINT IDENTITY(1,1) NOT NULL,
    BIT240269_Name        NVARCHAR(200)  NOT NULL,
    BIT240269_Description NVARCHAR(1000) NULL,
    CONSTRAINT PK_BIT240269_DishCategories PRIMARY KEY (BIT240269_Id)
);
GO


CREATE TABLE dbo.BIT240269_Dishes (
    BIT240269_Id              BIGINT IDENTITY(1,1) NOT NULL,
    BIT240269_Name            NVARCHAR(200)  NOT NULL,
    BIT240269_Price           NUMERIC(18,2)  NOT NULL,
    BIT240269_PreparationTime INT            NOT NULL,   -- phút
    BIT240269_IsAvailable     BIT            NOT NULL CONSTRAINT DF_BIT240269_Dishes_IsAvailable DEFAULT 1,
    BIT240269_Description     NVARCHAR(2000) NULL,
    BIT240269_DishCategoryId  BIGINT         NOT NULL,
    CONSTRAINT PK_BIT240269_Dishes PRIMARY KEY (BIT240269_Id),
    CONSTRAINT FK_BIT240269_Dishes_DishCategories
        FOREIGN KEY (BIT240269_DishCategoryId) REFERENCES dbo.BIT240269_DishCategories(BIT240269_Id),  -- không cho xóa loại nếu còn món
    CONSTRAINT CK_BIT240269_Dishes_Price CHECK (BIT240269_Price > 0),
    CONSTRAINT CK_BIT240269_Dishes_PreparationTime CHECK (BIT240269_PreparationTime > 0),
    CONSTRAINT UQ_BIT240269_Dish_Name_Category UNIQUE (BIT240269_Name, BIT240269_DishCategoryId)         -- tên không trùng trong cùng loại
);
GO


CREATE TABLE dbo.BIT240269_DishImages (
    BIT240269_Id          BIGINT IDENTITY(1,1) NOT NULL,
    BIT240269_ImageUrl    NVARCHAR(1000) NOT NULL,
    BIT240269_IsThumbnail BIT            NOT NULL CONSTRAINT DF_BIT240269_DishImages_IsThumbnail DEFAULT 0,
    BIT240269_DishId      BIGINT         NOT NULL,
    CONSTRAINT PK_BIT240269_DishImages PRIMARY KEY (BIT240269_Id),
    CONSTRAINT FK_BIT240269_DishImages_Dishes
        FOREIGN KEY (BIT240269_DishId) REFERENCES dbo.BIT240269_Dishes(BIT240269_Id) ON DELETE CASCADE
);
GO


CREATE UNIQUE INDEX UX_BIT240269_DishImages_OneThumbnail
    ON dbo.BIT240269_DishImages(BIT240269_DishId) WHERE BIT240269_IsThumbnail = 1;
GO


INSERT INTO dbo.BIT240269_DishCategories (BIT240269_Name, BIT240269_Description) VALUES
 (N'Món chính',     N'Các món ăn chính trong bữa'),
 (N'Đồ uống',       N'Nước uống, cà phê, trà'),
 (N'Tráng miệng',   N'Món ngọt ăn sau bữa chính');

INSERT INTO dbo.BIT240269_Dishes
 (BIT240269_Name, BIT240269_Price, BIT240269_PreparationTime, BIT240269_IsAvailable, BIT240269_Description, BIT240269_DishCategoryId) VALUES
 (N'Phở bò',        55000, 20, 1, N'Phở bò tái nạm nước dùng hầm xương',        1),
 (N'Cơm tấm sườn',  50000, 15, 1, N'Cơm tấm sườn nướng, bì, chả trứng',         1),
 (N'Bún chả',       60000, 25, 0, N'Bún chả Hà Nội (tạm ngừng bán)',            1),
 (N'Cà phê sữa đá', 25000,  5, 1, N'Cà phê phin pha sữa đặc',                   2),
 (N'Trà đào',       30000, 10, 1, N'Trà đào cam sả',                            2),
 (N'Chè khúc bạch', 35000, 12, 1, N'Chè khúc bạch hạnh nhân, vải',              3);


INSERT INTO dbo.BIT240269_DishImages (BIT240269_ImageUrl, BIT240269_IsThumbnail, BIT240269_DishId) VALUES
 (N'https://picsum.photos/seed/pho1/400/300',  1, 1),
 (N'https://picsum.photos/seed/pho2/400/300',  0, 1),
 (N'https://picsum.photos/seed/comtam/400/300', 1, 2);
GO

SELECT * FROM dbo.BIT240269_DishCategories;
SELECT * FROM dbo.BIT240269_Dishes;
SELECT * FROM dbo.BIT240269_DishImages;
GO
