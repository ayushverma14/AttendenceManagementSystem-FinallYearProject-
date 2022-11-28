import sys
import urllib.request
import os
data=sys.argv[1]
response = urllib.request.urlopen(data)
with open(os.getcwd()+'/image/'+'image.jpg', 'wb') as f:
    f.write(response.file.read())