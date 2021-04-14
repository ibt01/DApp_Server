//短信验证码-1234是万能验证码
curl http://127.0.0.1:8080/yinnihos/sendPhoneMsg     -X POST -d '{"phoneNumber":"+8615010913082"}' -H "Content-Type: application/json"

//register-hospital
hospital = 2;doctor = 3;drugstore=4;patient=5
curl http://127.0.0.1:8080/yinnihos/user/register  -X POST -d '{"phoneNum":"18510822965","phoneMessageCode":"1234", "loginPWD":"0000", "name":"Washington Hospital","hosRole":"2","hospitalDto":{"hospitalAddress":"Times Square Hall, New York", "establishTime":"1614911982000"}}' -H "Content-Type: application/json"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

//register-doctor
curl http://127.0.0.1:8080/yinnihos/user/register  -X POST -d '{"phoneNum":"15010913080","phoneMessageCode":"1234", "loginPWD":"0000", "name":"Lily","hosRole":"3","doctorDto":{"doctorGender":"1","hospitalBelong":"hospitaluserid5d93802b-7c31-4ed6-8d4f-6157a97a360b","doctorEmail":"uhdesk@126.com","expertise":"children"}}' -H "Content-Type: application/json"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

//register-drugstore
curl http://127.0.0.1:8080/yinnihos/user/register  -X POST -d '{"phoneNum":"15010913083","phoneMessageCode":"1234", "loginPWD":"0000", "name":"New York Pharmacy","hosRole":"4","drugstoreDto":{"drugstoreAddress":"206 1st Ave, New York · (212) 253-8686","establishTime":"1614911982000"}}' -H "Content-Type: application/json"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

//register-patient

curl http://127.0.0.1:8080/yinnihos/user/register  -X POST -d '{"phoneNum":"15010913084","phoneMessageCode":"1234", "loginPWD":"0000", "name":"Andy","hosRole":"5","patientDto":{"patientGender":"1","patientEmail":"uhdesk@126.com"}}' -H "Content-Type: application/json"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

uid="doctoruserid410377be-1527-4895-9f2e-c462fb66bb8a"
cookie="2fc6bf4a-5e8e-4b8b-b632-3532cdd24697"

uid="hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80"
cookie="86c019a5-434c-4d72-94e1-e91a9cfa3540"

uid="drugstoreuserid0f9067cf-7978-4e28-bb52-6be4d46016ed"
cookie="dc50f77e-d0f5-40ba-b6ce-d79483380843"

uid="patientuserid8667143b-ca27-41f0-8bf3-7ec9ab029632"
cookie="a4c8a778-d089-47cc-98a2-a4da9eab8bef"

uid="supermanageruseridxii2kdsl-skeis-23d-2dfsf2--sdf2--ss"
cookie="2fe6aa07-1854-46da-9324-17347c32b1a8"

//login
curl http://frp.chengxi.tech:8080/yinnihos/user/login  -X POST -d '{"phoneNum":"17165386295","phoneMessageCode":"1234"}' -H "Content-Type: application/json"
{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"userID":"doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25","cookie":"0f397404-4c20-416e-a722-fb1d6bcf2b60","myRole":3,"status":1,"statusEntity":3}}


#审核中的返回信息
{"code":"10001","msg":"Please wait, your account information is pending review","enMsg":"Please wait, your account information is pending review","extraMsg":null,"data":{"userID":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","cookie":null,"myRole":2,"status":null,"statusEntity":1}}
#注册信息被驳回
{"code":"10001","msg":"Please wait, your account information is pending review","enMsg":"The submission information is rejected, please edit and resubmit","extraMsg":null,"data":{"userID":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","cookie":null,"myRole":2,"status":null,"statusEntity":2}}


//获取我的信息-4种角色4种返回
curl http://127.0.0.1:8080/yinnihos/user/getMyInfo  -X GET -H  "my_uid:$uid" -H "my_cookie:$cookie"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","phoneNum":"18510822965","name":"Washington Hospital","headerPicUrl":null,"prescriptionRole":2,"doctorDto":null,"hospitalDto":{"hospitalAddress":"Times Square Hall, New York","establishTime":1614911982000},"drugstoreDto":null,"patientDto":null}}

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25","phoneNum":"15010913082","name":"Lily","headerPicUrl":null,"prescriptionRole":3,"doctorDto":{"doctorGender":1,"hospitalBelong":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","hospitalBelongName":"Washington Hospital","doctorEmail":"uhdesk@126.com","expertise":"children"},"hospitalDto":null,"drugstoreDto":null,"patientDto":null}}

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"drugstoreuserid0f9067cf-7978-4e28-bb52-6be4d46016ed","phoneNum":"15010913083","name":"Good DrugStore","headerPicUrl":null,"prescriptionRole":4,"doctorDto":null,"hospitalDto":null,"drugstoreDto":{"drugstoreAddress":"xxx xxx xxxsl 301","establishTime":null},"patientDto":null}}

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"patientuserid8667143b-ca27-41f0-8bf3-7ec9ab029632","phoneNum":"15010913084","name":"Andy","headerPicUrl":null,"prescriptionRole":5,"doctorDto":null,"hospitalDto":null,"drugstoreDto":null,"patientDto":{"patientGender":1,"patientEmail":"uhdesk@126.com"}}}

//上传药房图片文件
curl http://127.0.0.1:8080/yinnihos/file/upLoadFile  -H  "my_uid:$uid" -H "my_cookie:$cookie"  -F "file=@/Users/ibt/Documents/testdata/yyzz.jpeg"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":{"url":"http://127.0.0.1:8080/yinnihos/file/readfile/edu_202122_doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25yyzz.jpeg"}}


//创建药方-只有医生有这个权限
curl http://frp.chengxi.tech:8080/yinnihos/resep/createResep  -X POST -d '{"note":"please do it carefully","patientId":"patientuserida6529cca-0f5e-4a0f-9e04-2be18cb1c0a3","pictureUrl":"http://127.0.0.1:8080/yinnihos/file/readfile/edu_202122_doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25yyzz.jpeg","medicalItemList":[{"medicalName":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","quantityWhole":"20","usageOneTime":"one day three times,one time 3 pian"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

//获取我的药方（医生-药房-病人3个角色都可以获取药方）
curl http://127.0.0.1:8080/yinnihos/resep/getMyReseps  -X POST -d '{"id":"1","pageReq":{"page":"0","pageSize":"10"}}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

返回参数：
medicalJsonBase64 需要进行base64反解密
doctorResepOnChainTxid 医生开药赏脸txid
drugStoreResepOnChainTxid 药房取药上链txid

{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":{"resepDomains":[{"id":9,"patientId":"patientuserid8667143b-ca27-41f0-8bf3-7ec9ab029632","patientName":"Andy","patientPhoneNum":"15010913084","doctorId":"doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25","doctorName":"Lily","doctorPhoneNum":"15010913082","hospitalId":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","drugStoreId":null,"drugStoreName":null,"drugStorePhoneNum":null,"medicalJsonBase64":"W3sibWVkaWNhbE5hbWUiOiJob3NwaXRhbHVzZXJpZDBhYzYwMDVmLWYzYWUtNDc2Zi1hZTkwLTU5MWZmY2Y0YWE4MCIsInF1YW50aXR5V2hvbGUiOiIyMCIsInVzYWdlT25lVGltZSI6Im9uZSBkYXkgdGhyZWUgdGltZXMsb25lIHRpbWUgMyBwaWFuIn1d","pictureUrl":"http://127.0.0.1:8080/yinnihos/file/readfile/edu_202122_doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25yyzz.jpeg","status":null,"doctorResepOnChainTxid":"c2ad3ec69197383c1808a87278ca29e7f86d9cb8f432b31fb4580d841b40e8f1","drugStoreResepOnChainTxid":null,"created":1614687150406,"updated":1614687153127}],"totalLines":2}}


curl http://127.0.0.1:8080/yinnihos/resep/getResepById  -X POST -d '{"id":"11"}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"
//显示药方的二维码



返回参数：
resepsQr 用二维码方式显示出来，提供给药房扫码
curl http://127.0.0.1:8080/yinnihos/resep/showMyResepsQr  -X POST -d '{"resepId":"9"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":{"resepsQr":"38ef4a24-343a-41da-81b4-887dce17b68e"}}



curl http://127.0.0.1:8080/yinnihos/resep/getResetByQr  -X POST -d '{"resepsQr":"96686326-63d4-4921-bd40-d135734347ab"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

//药房扫码，并标记药方被取药了--药方不用用于重复取药
curl http://127.0.0.1:8080/yinnihos/resep/dealResetByQr  -X POST -d '{"resepsQr":"96686326-63d4-4921-bd40-d135734347ab"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}

//医生通过手机号搜索病人
curl http://127.0.0.1:8080/yinnihos/user/searchUserByPhone  -X POST -d '{"phoneNum":"150"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":{"id":"patientuserid8667143b-ca27-41f0-8bf3-7ec9ab029632","phoneNum":"15010913084","name":"Andy"}}

//获取待审核的用户类型列表
curl http://127.0.0.1:8080/yinnihos/user/getCheckTypes  -X GET -H  "my_uid:$uid" -H "my_cookie:$cookie"
医院用户获取的返回如下（只有医生）
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":[{"name":"doctor","role":3}]}
超级管理员用户获取的返回如下（有医院和药房）
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":[{"name":"drugstore","role":4},{"name":"hospital","role":2}]}


//获取待审核的账户
curl http://frp.chengxi.tech:8080/yinnihos/user/getUserToCheck  -X POST -d '{"userRole":"2"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":[{"id":"hospitaluserid0ac6005f-f3ae-476f-ae90-591ffcf4aa80","phoneNum":"18510822965","headerPicUrl":null,"name":"Ai kang hospital","status":1,"statusEntity":1,"prescriptionRole":2,"created":1614568440013,"updated":1614568440013}]}


uid="hospitaluserid5d93802b-7c31-4ed6-8d4f-6157a97a360b"
cookie="85405691-eba2-4849-bd03-9d2387c89070"

my_cookie: 85405691-eba2-4849-bd03-9d2387c89070
my_uid: hospitaluserid5d93802b-7c31-4ed6-8d4f-6157a97a360b

curl http://127.0.0.1:8080/yinnihos/user/getUserToCheckDetailInfo  -X POST -d '{"userId":"doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

//审核某个账户
请求参数
entityStatus;
statusToPermit = 1待审核;statusRejected = 2拒绝;statusPermitted = 3同意;
notes 备注
curl http://127.0.0.1:8080/yinnihos/user/doCheckSomeOne  -X POST -d '{"userId":"doctoruserid4470867c-4e81-4228-a78c-f05ac6550d6d", "entityStatus":"3", "notes":"information  right"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"
{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":null}


//通过医院名称模糊搜索医院
curl http://127.0.0.1:8080/yinnihos/user/searchHospitalByBluryName  -X POST -d '{"searchByName":"on"}]}' -H "Content-Type: application/json" -H  "my_uid:$uid" -H "my_cookie:$cookie"

{"code":"0","msg":"success","enMsg":"success","extraMsg":null,"data":{"userID":"doctoruserid237ea48e-d1d5-45d1-bb0c-83159fa41e25","cookie":"36206613-1799-41cb-892c-b32a19919da6","myRole":3,"status":1,"statusEntity":3}}