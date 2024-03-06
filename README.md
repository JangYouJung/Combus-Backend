# 🚌 Combus - Google Solution Challenge 2024
## 🚎 What is Combus? 
Combus provides solutions to lower the inequality on using bus in South Korea.
It provides bus reservation and boarding assistance services for the disabled, 
especially AI bus route number detection and Speech-To-Text reservation for the visually impaired.
<br></br>

## 📽️ Combus Demo Video
### [![컴버스 목업](https://github.com/JangYouJung/Combus-Backend/assets/80906691/85a9ed1b-b224-43be-b41b-e86e8ee646a7)](https://www.youtube.com/watch?v=fFhG3aAYjc4)
<br></br>

## 📑 Combus System Architecture
![스크린샷 2024-02-28 02 34 12](https://github.com/JangYouJung/Combus-Backend/assets/80906691/d7842215-0212-4665-87fd-6a48b0f6b78b)
<br></br>

## 🚏 Combus Team Crew 
|               🧑‍💻 [YouJung Jang](https://github.com/JangYouJung)               |               🧑‍💻 [JiHyun Lee](https://github.com/JIHYUN2EE)               |               🧑‍💻 [SinYoung Kang](https://github.com/sinyoung6491)                |               🧑‍💻 [YuNa Jeong](https://github.com/13b13)                 |
| :---------------------------------------------------------------: | :--------------------------------------------------------------: | :-------------------------------------------------------------: | :-----------------------------------------------------------: |
| <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/202e60b8-d715-4d83-a372-a21b4b153b07" width="200" height="200"/>| <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/f3616fda-5d66-43e6-8438-5e47b07da873" width="200" height="200"/> | <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/33179ee5-b52f-4e33-9418-76ac80b15313" width="200" height="200"/> | <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/3f8a37bd-631e-40d0-b758-417580790a68" width="200" height="200"/>
|                  <p align = "center">`Leader / Backend`                  |                 <p align = "center">`Backend / AI`                  |                 <p align = "center">`Bus Driver App Frontend`                 |                <p align = "center">`Passenger App Frontend`                |

<br></br>

## 📝 How to setup Local Combus Server
### 1. Clone [Combus Backend Repository](https://github.com/GDSC-COMBUS/Combus-Backend)
### 2. Get the Service key from each APIs below
-----------------------------------------------------------------
### [1] Korea Public Data Portal for Bus Information Open API
To get the service key from Korea Public Data Portal, Sign up and apply for open APIs below, then you'll get the service key <br/>
copy the service key and then paste it at `src/main/resources/applicaion.yml` line 16
|Open API name| application link |
|:--:|:--:|
|Get Seoul Bus Arrival Info |https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15000314|
|Get Seoul Bus Position Info|https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15000332|
|Get Seoul Bus Route Info |https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15000193|
|Get Seoul Bus Stop Info|https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15000303|
#### [👉Click here ](https://yuejeong.tistory.com/63) for more detailed infromation about how to apply!

-----------------------------------------------------------------
### [2] Google Video Intelligence API
Set up the project through [GCP console](https://cloud.google.com/video-intelligence) and fill out the combusVideo JSON file with your own keys.<br/>
📁 `src/main/resources/combusvideo-413101-ccb434c20a8f.json` 

-----------------------------------------------------------------
### 3. Execute the init.sql script on your local database server.
📁 file location: `src/main/resources/init.sql` 

-----------------------------------------------------------------
### 4. Now you're ready to run the Combus Server!  
📲 Try out with Combus UI from [Combus Frontend Repository](https://github.com/GDSC-COMBUS/Combus-Frontend)

<br></br>


## 🗂️ App Types 
### Check out the code at [Combus Frontend Repository](https://github.com/GDSC-COMBUS/Combus-Frontend)
|  | Passenger App | Bus Driver App |
|:---:|:---|:---|
| App | MyApplication | ComBus_DriverApp |
| **User** | Visually impaired and wheelchair users | Bus Driver |
| **Function** | - Select boarding and drop off stops through GPS location<Br/> - Bus boarding reservation, voice reservation, and voice service<Br/> - Bus number verification service via camera  | - Check bus real-time location and scroll through routes automatically<Br/> - Next stop reservation info pop-up notification |

<br></br>

## 🎬 How to use Combus
### 📱 User App
🔹 **General reservation method**
1. Enter the user verification code and press 'START' to log in.
2. Press 'CREATE' to select the boarding stop among the nearby stops based on the current location.</br>
   (✋Set the current location to South Korea, Seoul) 
3. Select the target bus from the bus list passing through the selected boarding stop.
4. Choose a drop-off stop among the bus stops.
5. Reservation completed

🔹 **Voice reservation method**
1. Press 'VOICE' to voice the user verification code and press the 'START' button to log in.
2. Press 'CREATE WITH VOICE' to start the voice reservation.
3. Listen to the voice description and press 'START' to recognize the voice.
4. Designate boarding stop, bus, and drop-off stop and complete the reservation.

🔹 **How to verify bus number after booking**
1. Press 'WEBCAM FOR BUS VERIFICATION' to check the camera screen.
2. When the bus arrives, press 'START CAPTURE' to start recording the video, and press 'STOP CAPTURE' to stop recording the video.
3. Check the information below the 'START CAPTURE' button. It'll tell you if it's right bus or not.

🔹 **How to use it after making a reservation**
1. When boarding, press 'BOARDING CONFIRM' to change the status.
2. When you get off, change the status by pressing 'DROPPING OFF CONFIRM'.

-----------------------------------------------------------------

### 📱 Bus driver App
🔹 **How to check location and reservation information**
1. Enter the driver verification code and press 'LOGIN' to log in.
2. It scrolls automatically and shows the current bus location with the yellow bus stamp. (Automatic refresh every 30 seconds)
3. Check the reservation information for each stop. <br/>
   (Reservation: Count of boarding passengers / Drop off: Count of dropping off passengers / Wheelchair shape on the right side if there is a wheelchair reservation)
4. Press each bus stop to check the boarding and drop off passenger's information at each stop.
5. On the home screen, if the next bus stop has any boarding or drop off passenger based on the current bus location, a pop-up notification appears for 10 second. Press Auto Close or X to close it.

<br></br>

## 📑 API Docs
### [Combus API Document](https://cypress-overcoat-478.notion.site/API-31e4dbe46bc748e78e9d78c9ebf46f78?pvs=4)
<br></br>

## 🎨 Design
### 🖼️ [Figma](https://www.figma.com/file/pKEx9GyBsCvqL84lFxsSZI/2024-Google-Solution-Challenge---%EC%9E%A5%EC%95%A0%EC%9D%B8-%EB%B2%84%EC%8A%A4-%EB%8F%84%EC%9A%B0%EB%AF%B8?type=design&node-id=3%3A163&mode=design&t=KuWlg1gsUkA1h4xp-1)

