from PIL import Image

files = ['aim.png', 'brown.png', 'chrome.png', 'facebook.jpg', 'messenger.jpg', 'pintrest.png', 'purple.png', 'skype.png', 'target.jpg', 'weather.jpg']

for filename in files:

    img = Image.open('drawable/'+filename)
    img = img.convert("RGBA")
    datas = img.getdata()

    newData = []
    for item in datas:
        if item[0] == 255 and item[1] == 255 and item[2] == 255:
            newData.append((255, 255, 255, 0))
        else:
            newData.append(item)

    img.putdata(newData)
    img.save("drawable2/"+filename.split('.')[0]+".png", "PNG")
