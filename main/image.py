from PIL import Image

# Mở ảnh PNG

image = Image.open(r"C:\Users\ADMIN\Downloads\game2D\res\BOSS\skill_2_right\s6.png")

# Lật ảnh theo chiều ngang (như nhìn qua gương)
mirrored_image = image.transpose(Image.FLIP_LEFT_RIGHT)

# Lưu ảnh đã lật
mirrored_image.save("s6.png")

# Hiển thị ảnh đã lật
mirrored_image.show()
