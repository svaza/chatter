# Chatter
A simple chat client over LAN

# Future Enhancements
I know that there are many good file transfer apps, and in the world of cloud tech there is no need of transferring files over LAN. 
This project was developed years back i.e 2010 by me, During my colledge days as part of my final semester Diploma. However it isnt maintained
by me right now.

Feel free to clone this repo and raise BUGS/Pull requests I am open for changes.

# v1.2 new features and fixed bugs (from v1.1) :
1. config.xml,users.xml path-configuration through settings.ini file
2. BUG-FIX:
   When file scanner starts file scan then it cannot be stopped
   untill file scan is finished.
3. An "X" button provided over chat board tab for closing the tab.

# What is config/config.xml and config/users.xml ?
config.xml stores your applications configuration.

## Here is short description:
When your application starts it also starts 5 servers. Don't worry about name. SERVERS they are just mini servers. just a Thread you can also understand like
5 small programmes running asynchronously why not they are multithreaded listening to ports for recieving notifications from clients.
don't confuse by looking config.xml just understand the following points.

1. <user> (line no 3):
   just leave it.
2. <ip-address>127.0.0.1</ip-address> (line no 4):
   127.0.0.1 should be replaced by your IP address you can also leave
   it as it is.
3. <user-name>James</user-name> (line no 5):
   James should be replaced by your name, or any thing you like.
4. <server-port>8001</server-port> (line no 6):
   8001 is port, OK am sorry if you did't understood it.
   just assume it as number between 20,000 and 65,000, take your lucky number
   If it gives Exceptions then change it. But remember it should be Whole number
   no decimal,commas,alphabets,special characters allowed here.If intrested then read TECHNICAL.TXT file.
5. <filetransfer-server-port>8003</filetransfer-server-port> (line no 8):
   (Refer point 4).
6. <filerecieve-server-port>8004</filerecieve-server-port> (line no 9):
   (Refer point 4).
7. <instance-port>9000</instance-port> (line no 10):
   (Refer point 4).
8. <buffer-size>2</buffer-size> (line no 11):
    Leave it, if intrested then read TECHNICAL.TXT file.
9. </user> (line no 14):
   (Refer point 1).

users.xml file is same as config.xml but each user config should be within
<users>..</users> tag STRICTLY.

Don't forget to update users.xml file if config.xml of any user changes. Its not mandatory that config.xml file of all users should be unique.



# How can you tackle the problem of continuosly updating the users.xml or config.xml?
you can create a shared directory on a PC and update respected path in settings.ini file

Eg:
users=\\192.168.0.254\\java projects\\NetBeans\\Java SE\\Chatter\\jOctopus 1.0\\config\\users.xml
Note that statements preceeding "#" will be marked as comments.

# History
## Chatter v1.0 :
Chatter debuts.


## jOctopus v1.1 OR Chatter 1.1  :
The release name was jOctopus, However it didnt continued, So there are just versions now.. :(
### BUG-FIXES:
1. when user is not connected at that time, when new chat
   is recieved by user, it results in null pointer exception.
2. when new chat window is popup then context-menuitems gets added to popup
   menu for each window.
### NEW FEATURES
1. added CLEAR button in file transfer tab.
2. recieving files from multiple clients asynchronously.

# CONFIGURATION 
## config.xml & users.xml structure


1. <ip-address>127.0.0.1</ip-address> (line no 4):
Its the ipaddress of your computer, If its over LAN your PC have been given
a unique IP Address. You can contact your N/W Administrator for more details.


2. <server-port>8001</server-port> (line no 6):
I have developed a server thread for recieving CHAT messages, So that chat server
listens at this port. If an application is already using this port then you can
take some other higher port.

4. <filetransfer-server-port>8003</filetransfer-server-port> (line no 8):
The File Transfer Server will be started at this port. This server does the job of
trsnsfering the files to requester. It creates a Separate thread for each user
requests for handling requests. It transfers the files based on the file path given
by user.This might prove useful if you want other programmes developed in JAVA,.NET
PYTHON or other programming want to requests the file from it.


5. <filerecieve-server-port>8004</filerecieve-server-port> (line no 9):
The File reciever port will be started at this port.


6. <instance-port>9000</instance-port> (line no 10):
You can run only 1 instance of this application at a time. When application
starts at that time it binds this port.So when you start the application next time it would
not be able to bind at this port, So it knows that other instance is already running.



7. <buffer-size>2</buffer-size> (line no 11):
If the file size is greater then buffer-size then it will transfer the file in chunks.
means the size given in buffer size. For example if file size is 30 MB and buffer-size is 2
then it will send the file in chunks and size of each chunk will be the value of buffer-size.
The value of buffer-size should be in MB.
