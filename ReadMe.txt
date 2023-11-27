NASA APOD WallPaper of the day updater

Purpose: This program calls upon the daily image provided by the NASA APOD API. It takes the image and stores it in the iamge folder and updates the users background to 
the current image. This application works for Windows 11. 

Implimentation: We call the Nasa rest API using java http client. Then we handle the windows update using the windows C libraries. These are connected using Java JNI
Java Native Interface. 

Author: Andrew Ho
