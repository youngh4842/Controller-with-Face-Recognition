import dlib
import cv2
import numpy as np
import time
import socket

#socket_client.ver
#HOST = '172.30.1.40'
HOST = '192.168.1.108'
PORT = 8088

client_socket = socket.socket(socket.AF_INET,socket.SOCK_STREAM) 
client_socket.connect((HOST, PORT)) 

detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor('C:/Users/haneu/fin_pro/modle/shape_predictor_68_face_landmarks.dat')
face_recog = dlib.face_recognition_model_v1('C:/Users/haneu/fin_pro/modle/dlib_face_recognition_resnet_model_v1.dat')

#인코딩이 되어있거 가져옴 
descs = np.load('C:/Users/haneu/fin_pro/a_project/a_descs.npy', allow_pickle=True)[()]
#descs = np.load('C:/Users/haneu/fin_pro/a_project/a5_descs.npy', allow_pickle=True)[()]
#descs = np.load('C:/Users/haneu/fin_pro/a_project/a10_descs.npy', allow_pickle=True)[()]
#descs = np.load('C:/Users/haneu/fin_pro/a_project/a20_descs.npy', allow_pickle=True)[()]

#들어온 영상에서 이미지 찾기 
def encode_faces(image):

    faces = detector(image,1)

    if len(faces) == 0:
        return np.empty(0)

    for k, d in enumerate(faces):
        land = predictor(image, d)
        face_decriptor = face_recog.compute_face_descriptor(image, land)

        return np.array(face_decriptor)

#실시간으로 해보자
stream_cap = cv2.VideoCapture("http://192.168.1.105:8090/?action=stream")
#stream_cap = cv2.VideoCapture(0)

_, img_bgr = stream_cap.read() 
padding_size = 0
resized_width = 1920
video_size = (resized_width, int(img_bgr.shape[0] * resized_width // img_bgr.shape[1]))
output_size = (resized_width, int(img_bgr.shape[0] * resized_width // img_bgr.shape[1] + padding_size * 2))

#show the time
def get_time():
    tm = time.localtime()
    tm_p = time.strftime('%Y-%m-%d-%H-%M-%S-%p', tm)
    print(tm_p)
    return tm_p

text_w = open('C:/Users/haneu/fin_pro/a_project/a_result.txt', 'w')

#check the image
checks =0
check_sum=0


while True:
    ret, img_bgr = stream_cap.read()

    if not ret:
        break

    #img_bgr = cv2.resize(img_bgr, video_size)
    img_rgb = cv2.cvtColor(img_bgr, cv2.COLOR_BGR2RGB)

    faces = detector(img_rgb, 1)

    for k, d in enumerate(faces):
        #사각형위치 찾아놓기
        rect = ( (d.left(), d.top()), (d.right(), d.bottom()) )

        land = predictor(img_rgb, d)
        face_descriptor = face_recog.compute_face_descriptor(img_rgb, land)

        last_found = {'name': 'unknown', 'dist': 0.6, 'color': (0,0,255)}    

        for name, saved_desc in descs.items():
            dist = np.linalg.norm([face_descriptor] - saved_desc, axis=1)

            if dist < last_found['dist']:
                last_found = {'name': name, 'dist': dist, 'color': (255,255,255)}
                tm_p = get_time() #인식될때 시간을 찍음
                print(dist)
                
                
        cv2.rectangle(img_bgr, pt1=(d.left(), d.top()), pt2=(d.right(), d.bottom()), color=last_found['color'], thickness=2)
        cv2.putText(img_bgr, last_found['name'], org=(d.left(), d.top()), fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1, color=last_found['color'], thickness=2)
        print('  :  ' + last_found['name']) #이름 출력

        #연속으로 다섯번의 이름이 입력되었을 때에 WWWWWWWW출
        if (checks ==0):
            first_name = last_found['name']

        if (check_sum < 4):
            if (first_name == last_found['name']):
                check_sum +=1
                checks+=1
            else:
                checks = 0
                check_sum=0
        else:
            print(first_name+'WWWWWWWWWWWWWWWWWWWWW')
            checks = 0
            check_sum=0

            #소켓 통신으로 메세지 보내기
            # 메시지를 전송합니다.
            s_data = first_name
            #client_socket.sendall(s_data.encode()) #개인
            client_socket.send(s_data.encode()) #쓰레드

            # 메시지를 수신합니다.
            r_data = client_socket.recv(1024)
            print('Received', repr(r_data.decode()))


        #텍스트파일에 저장
        text_w.write(last_found['name'] +" " + tm_p + '\n')

        


  #writer.write(img_bgr)

    cv2.imshow('img', img_bgr)
    if cv2.waitKey(1) == ord('q'):
       break
    
client_socket.close()
    
text_w.close()
stream_cap.release()
cv2.destroyAllWindows()
#writer.release()

        


