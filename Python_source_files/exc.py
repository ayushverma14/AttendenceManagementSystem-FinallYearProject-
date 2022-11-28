import os
import pandas as pd
from pathlib import Path
from openpyxl import load_workbook
from datetime import date
import csv
import sys  
path=os.getcwd()+'/attendence/'+sys.argv[5]+'/'+sys.argv[6]+'.csv'
print(path)
fle = Path(path)
fle.touch(exist_ok=True)
rows=[]
fields=[]
def search(list,item):
    for l in list:
        if len(l)>=2 and l[2]==item:
           print(l[2])
           print(item)
           print(l[2]==item)
           return 1
    return 0
format=['Serial no.','Name','Registration  No','Mobile','Email id']        
with open(path,'r') as file:
    reader=csv.reader(file)
    
    for row in reader:
        rows.append(row)
    print(rows)
    print(len(rows))
    id=str(len(rows)+1)

if len(rows)==0:

    rows.append(format)
    

    
ns=[id,sys.argv[1],sys.argv[2],sys.argv[3],sys.argv[4]]
#ns=[]
fieldlength=len(rows[0])

i=0


while i<(fieldlength-5):
    ns.append('Absent')
    i+=1

result=search(rows, sys.argv[2])

rows.append(ns)
if result==0:

  with open(path,'w',newline='') as file:
    csvwriter = csv.writer(file)
    csvwriter.writerows(rows)
  print('Added to sheet')  
else:
    print('Already exists')


