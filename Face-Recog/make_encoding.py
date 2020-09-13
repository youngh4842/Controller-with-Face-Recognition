import dlib
import cv2
import numpy as np

#이미지 비교를 위해서 그래프 그리기 위한 module
import matplotlib.pyplot as plt
import matplotlib.patches as patches
import matplotlib.patheffects as path_effects

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("C:/Users/haneu/fin_pro/modle/shape_predictor_68_face_landmarks.dat")
face_recog = dlib.face_recognition_model_v1("C:/Users/haneu/fin_pro/modle/dlib_face_recognition_resnet_model_v1.dat")

#dect & predic(landmarks)
def find_faces (image) :

    #얼굴의 위치 찾아보기 
    faces = detector(image, 1)

    if len(faces) == 0:
        return np.empty(0)

    #rets는 사각형 처리, lands는 68개의 landmarks
    rects, lands = [],[]
    lands_np = np.zeros((len(faces), 68, 2), dtype=np.int)

    for k, d in enumerate(faces):

        #사각형위치 찾아놓기
        rect = ( (d.left(), d.top()), (d.right(), d.bottom()) )
        rects.append(rect)

        #landmark
        land = predictor(image, d)
        lands.append(land)

        #convert dlib shape to numpy array
        for i in range(0, 68):
            lands_np[k][i] = (land.part(i).x, land.part(i).y)


    return rects, lands, lands_np


#use resnet
def encode_faces(image, lands):

    face_descriptors = []
    for land in lands:
        face_descriptor = face_recog.compute_face_descriptor(image, land)
        face_descriptors.append(np.array(face_descriptor))

    return np.array(face_descriptors)


#image path
#image_paths =['C:/Users/haneu/younghj/', 'C:/Users/haneu/JS/', 'C:/Users/haneu/eunhl/']
#names = ['YOUNGHEE', 'JUNGSUK', 'HYEEUN']

image_pathe =(r"C:\Users\haneu\fin_pro\a_project/")
image_paths={
    'YOUNGHEE' : image_pathe+'younghee/',
    'BOSUNG': image_pathe+'bosung/',
    'SUJONG' : image_pathe+'sujong/',
    
    'HYEEUN' : image_pathe+'haeeun/'    
    
    }

img_num = 20
img_type = '.jpg'

#image result
descs = {
    'YOUNGHEE' : None,
    'BOSUNG' : None,
     'SUJONG' : None,
     
     'HYEEUN' : None

     
     }


for name, image_path in image_paths.items():

    i=0
    print(name)
    for idx in range (img_num):

        img_p = image_path+str(idx+1)+img_type
        #print(img_p)
        img_bgr = cv2.imread(img_p)
        img_rgb = cv2.cvtColor(img_bgr,cv2.COLOR_BGR2RGB)

        _, img_lands, _ = find_faces(img_rgb)
        descs[name] = encode_faces(img_rgb, img_lands)[i]
        
        print( descs[name][i])

    i+=1


#인코딩파일 저장 
np.save('C:/Users/haneu/fin_pro/a_project/a_descs.npy', descs)
print(descs)

#이미지 삽입해서 확인하기
img_bgr = cv2.imread('C:/Users/haneu/younghj/younghee.jpg')
img_rgb = cv2.cvtColor(img_bgr,cv2.COLOR_BGR2RGB)
rects, img_lands, _ = find_faces(img_rgb)
descriptors = encode_faces(img_rgb, img_lands)


#Visualize output
fig, ax = plt.subplots(1, figsize=(20, 20))
ax.imshow(img_rgb)

for i, desc in enumerate(descriptors):

    found = False
    for name, saved_desc in descs.items():
        dist = np.linalg.norm([desc] - saved_desc, axis=1) #유클리디안 거리

        #그래프에서 원본과의 거리차의가 0.6미만이라면 얼굴인식확정
        if dist < 0.6:
            found = True

            #이름을 출력해준다.
            text = ax.text(rects[i][0][0], rects[i][0][1], name,
                    color='b', fontsize=40, fontweight='bold')
            text.set_path_effects([path_effects.Stroke(linewidth=10, foreground='white'), path_effects.Normal()])
            rect = patches.Rectangle(rects[i][0],
                                 rects[i][1][1] - rects[i][0][1],
                                 rects[i][1][0] - rects[i][0][0],
                                 linewidth=2, edgecolor='w', facecolor='none')
            ax.add_patch(rect)

            print(name)

            break

    if not found:
        ax.text(rects[i][0][0], rects[i][0][1], 'unknown',
                color='r', fontsize=20, fontweight='bold')
        rect = patches.Rectangle(rects[i][0],
                             rects[i][1][1] - rects[i][0][1],
                             rects[i][1][0] - rects[i][0][0],
                             linewidth=2, edgecolor='r', facecolor='none')
        ax.add_patch(rect)

plt.axis('off')
#plt.savefig('C:/Users/haneu/a_project/output_re1.jpg') #사각형과 이름이 적혀진 비교파일을 새로저장.
plt.show()

































