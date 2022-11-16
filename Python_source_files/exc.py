import os
import pandas as pd
from pathlib import Path
from openpyxl import load_workbook
import csv  
path=os.getcwd()+'/data.csv'
print(path)
fle = Path(path)
fle.touch(exist_ok=True)
rows=[]
fields=[]
with open(os.getcwd()+'/data.csv','r') as file:
    reader=csv.reader(file)
    fields=next(reader)
    for row in reader:
        rows.append(row)

    print(len(rows))    

    