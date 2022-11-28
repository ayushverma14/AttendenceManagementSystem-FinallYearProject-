from base64 import encode
from datetime import date
import numpy as np
import glob as gb
import face_recognition as fr
import os
import cv2
import sys
import csv

path = os.getcwd()+'/'+sys.argv[1]
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
#print(known_face_names)

def attendence(img):
    nameList =[]
   
        
    today = date.today()
    d1 = today.strftime("%d/%m/%Y")
    d1=[d1]
    nameList.append(d1)
    for line in img:
            entry = line

            if entry not in nameList:
               entry=[entry]
               nameList.append(entry)
    print(nameList)      
        # if img not in nameList:
        #     f.write(f'\n{img}')
    with open(os.getcwd()+'/attendence.csv','w',newline='') as f:
        
         csvwriter = csv.writer(f)
         for l in nameList:

            csvwriter.writerow(l)
                             
               


#Loading the test image
target_img=fr.load_image_file(os.getcwd()+'/test_imgs/'+sys.argv[2])
target_encoding=fr.face_encodings(target_img)
locations=fr.face_locations(target_img)

cv2.namedWindow('Webcam_facerecognition',cv2.WINDOW_NORMAL)
imS = cv2.resize(target_img, (960, 540))

#test_snippet
# cv2.imshow('Webcam_facerecognition', imS)
# cv2.waitKey(0)

#finding corresponding face to the target
attend=[]
for (top, right, bottom, left), face_encoding in zip(locations,target_encoding):

        matches = fr.compare_faces(known_face_encondings, face_encoding)
        print(matches)
        name = "Unknown"

        face_distances = fr.face_distance(known_face_encondings, face_encoding)
        print(face_distances)
        best_match_index = np.argmin(face_distances)
        print(best_match_index)
        if matches[best_match_index] and face_distances[best_match_index]<0.5:
            name = known_face_names[best_match_index]
        if name != 'unknown' and name not in attend:
            attend.append(name)

        cv2.rectangle(target_img, (left, top), (right, bottom), (0, 0, 255), 2)

        cv2.rectangle(target_img, (left, bottom -35), (right, bottom), (0, 0, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_SIMPLEX
        cv2.putText(target_img, name, (left + 6, bottom - 6), font, 1, (255, 255, 255), 2)

cv2.imshow('Webcam_facerecognition', target_img)

    # if cv2.waitKey(1) & 0xFF == ord('q'):
    #     break
cv2.waitKey(0)
# video_capture.release()
cv2.destroyAllWindows()
attendence(attend)
print(attend)