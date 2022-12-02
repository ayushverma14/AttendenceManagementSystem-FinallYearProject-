import smtplib
import sys

sender = 'verma14ayush@gmail.com'
receivers = [sys.argv[1]]

message = """From: From Person <from@fromdomain.com>
To: To Person <to@todomain.com>
Subject: Short Attendence

This is to inform you have short attendence int the respective department.
"""+sys.argv[2]
password='qydnpkhzbexzrlfa'
try:
    smtpObj = smtplib.SMTP('smtp.gmail.com',587)
    smtpObj.starttls()
    smtpObj.login(sender,password)
    smtpObj.sendmail(sender, receivers, message)         
    print ("Successfully sent email")
except  Exception:
    print ("Error: unable to send email ")