import os
import pandas as pd
from pathlib import Path
import csv
import sys  

path=os.getcwd()+'/attendence.csv'
rows=[]
fields=[]
with open(path,'r') as file:
    reader=csv.reader(file)
    
    for row in reader:
        rows.append(row)

rows1=[]
path = os.getcwd()+'/attendence/'+sys.argv[1]+'/'+sys.argv[2]+'.csv'
with open(path,'r') as file:
    reader=csv.reader(file)
    
    for row in reader:
        if len(row)!=0:
          rows1.append(row)
i=0
list=[]
for p in rows:
    list.append(p[0])
for row in rows1:
    if i>0:
        if len(row)>2 and row[2] in list:
            row.append('Present')
        else :
            row.append('Absent')
    else :
        row.append(rows[0][0])      
    i+=1

with open(path,'w',newline='') as file:
    csvwriter = csv.writer(file)
    csvwriter.writerows(rows1)