from base64 import encode
import numpy as np
import glob as gb
import face_recognition as fr
import os
import cv2

video_capture = cv2.VideoCapture(0)
path = os.getcwd()+'/image'
list = os.listdir(path)
print(list)
known_face_encondings = []
known_face_images=[]
known_face_names =[]
for img in list:
    
   
   curimg=cv2.imread(f'{path}/{img}')
   curimg2=cv2.cvtColor(curimg,cv2.COLOR_BGR2RGB);
   encode = fr.face_encodings(curimg2)[0];
   known_face_encondings.append(encode)
   known_face_images.append(curimg)
   known_face_names.append(os.path.splitext(img)[0])

#print(known_face_encondings)
print(known_face_names)

def attendence(img):
    with open(os.getcwd()+'/attendence.csv','r+') as f:
        myDataList = f.readlines()
        nameList =[]
        for line in myDataList:
            entry = line
            nameList.append(entry)
        if img not in nameList:
            f.write(f'\n{img}')



while True: 
    ret, frame = video_capture.read()

    rgb_frame = frame[:, :, ::-1]

    face_locations = fr.face_locations(rgb_frame)
    face_encodings = fr.face_encodings(rgb_frame, face_locations)

    for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):

        matches = fr.compare_faces(known_face_encondings, face_encoding)

        name = "Unknown"

        face_distances = fr.face_distance(known_face_encondings, face_encoding)

        best_match_index = np.argmin(face_distances)
        if matches[best_match_index]:
            name = known_face_names[best_match_index]
        if name != 'unknown':
            attendence(name)

        cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)

        cv2.rectangle(frame, (left, bottom -35), (right, bottom), (0, 0, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_SIMPLEX
        cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

    cv2.imshow('Webcam_facerecognition', frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video_capture.release()
cv2.destroyAllWindows()
