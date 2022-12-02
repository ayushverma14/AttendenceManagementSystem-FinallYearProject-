import os
import sys
import csv
path=os.getcwd()+'/attendence/'+sys.argv[1]+'/'+sys.argv[2]+'.csv';
rows=[]
with open(path,'r') as file:
    reader=csv.reader(file)
    
    for row in reader:
        rows.append(row)
res=[]        
for row in rows:
    count=0
    opt=len(row)-5
    for entry in row:
        if entry=='Absent' :
          count=count+1
    if count> opt/4:
        ss=sys.argv[1]+"/"+sys.argv[2]
        ll=sys.argv[2].split(' ')
        st=""
        for s in ll:
            st=st+s

        r1=[st,'',row[1],'',row[4],'']
        res.append(r1)
print(res)