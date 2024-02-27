# 🚌 Combus - Google Solution Challenge 2024
## 🚎 What is Combus? 
Combus provides solutions to lower the inequality on using bus in South Korea.
It provides bus reservation and boarding assistance services for the disabled, 
especially AI bus route number detection and Speech-To-Text reservation for the visually impaired.
<br></br>

## 📽️ Combus Demo Video
### [<img width="1509" alt="스크린샷 2024-02-25 02 42 48" src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/463a87c4-0caa-4f99-b470-2c9c0190f704">](https://www.youtube.com/watch?v=fFhG3aAYjc4)
<br></br>



## 🚏 Combus Team Crew 
|               🧑‍💻 [YouJung Jang](https://github.com/JangYouJung)               |               🧑‍💻 [JiHyun Lee](https://github.com/JIHYUN2EE)               |               🧑‍💻 [SinYoung Kang](https://github.com/sinyoung6491)                |               🧑‍💻 [YuNa Jeong](https://github.com/13b13)                 |
| :---------------------------------------------------------------: | :--------------------------------------------------------------: | :-------------------------------------------------------------: | :-----------------------------------------------------------: |
| <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/202e60b8-d715-4d83-a372-a21b4b153b07" width="200" height="200"/>| <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/f3616fda-5d66-43e6-8438-5e47b07da873" width="200" height="200"/> | <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/33179ee5-b52f-4e33-9418-76ac80b15313" width="200" height="200"/> | <img src="https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/3f8a37bd-631e-40d0-b758-417580790a68" width="200" height="200"/>
|                  <p align = "center">`Leader / Backend`                  |                 <p align = "center">`Backend / AI`                  |                 <p align = "center">`Bus Driver App Frontend`                 |                <p align = "center">`Passenger App Frontend`                |

<br></br>

## 📝 How to setup Local Combus Server
### 1. Clone this repository
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
Try out with Combus UI from [Combus Frontend Repository](https://github.com/GDSC-COMBUS/Combus-Frontend)
<br></br>

## 📑 API Docs
### [Combus API Document](https://cypress-overcoat-478.notion.site/API-31e4dbe46bc748e78e9d78c9ebf46f78?pvs=4)
<br></br>

## 🚏 Git Convention
### Git Flow
![gitflow](https://github.com/GDSC-COMBUS/Combus-Backend/assets/80906691/4828d2ee-b192-4eca-96b4-96204698edcf)
