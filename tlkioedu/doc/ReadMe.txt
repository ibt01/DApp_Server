//发送短信
curl http://127.0.0.1:8080/yinniedu/sendPhoneMsg     -X POST -d '{"phoneNumber":"+8615010913082"}' -H "Content-Type: application/json"
{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":null}


//学校注册
curl http://127.0.0.1:8080/yinniedu/user/register  -X POST -d '{"phoneNum":"17165386298","phoneMessageCode":"1234", "loginPWD":"0000", "eduRole":"2","schoolDto":{"schoolName":"Qinghua University","schoolAddress":"Beijing haidian","schoolCode":"1","schoolEmail":"uhdesk@126.com"}}' -H "Content-Type: application/json"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":null}

{"code":"6","msg":"当前手机号已经注册过","enMsg":"PHONE_NUM_REGISTERED","extraMsg":null,"data":null}%

//学生注册
UserGender:male = 1;female = 2;
curl http://127.0.0.1:8080/yinniedu/user/register  -X POST -d '{"phoneNum":"17165386298","phoneMessageCode":"1234", "loginPWD":"0000", "eduRole":"1","studentDto":{"studentName":"chenlain","studentBirthday":"1613474864000","studentGender":"1","studentCode":"1234567","studentEmail":"ibt2015@126.com","schoolCode":"1"}}' -H "Content-Type: application/json"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":null}

{"code":"13","msg":"没有这所学校","enMsg":null,"extraMsg":null,"data":null}

//登录-学生

curl http://127.0.0.1:8080/yinniedu/user/login  -X POST -d '{"phoneNum":"17165386298","phoneMessageCode":"1234"}' -H "Content-Type: application/json"
{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"userID":"useriddb07cd53-962c-4218-a0de-290109ceed20","cookie":"a14b088e-333c-471d-9c76-25eb8b3f5cc6","myRole":1,"status":1}}

//登录-学校
curl http://127.0.0.1:8080/yinniedu/user/login  -X POST -d '{"phoneNum":"18510822965","phoneMessageCode":"1234"}' -H "Content-Type: application/json"
{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"userID":"userid1a80eccc-352e-4940-8df0-7a437e972a4a","cookie":"9e8dd5c8-25e3-41f1-999b-8742896894a2","myRole":2,"status":1}}%

//我的信息-学生
curl http://127.0.0.1:8080/yinniedu/user/getMyInfo  -X GET -H "my_uid:useriddb07cd53-962c-4218-a0de-290109ceed20"  -H "my_cookie:a14b088e-333c-471d-9c76-25eb8b3f5cc6"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"useriddb07cd53-962c-4218-a0de-290109ceed20","phoneNum":"15010913082","headerPicUrl":null,"eduRole":1,"studentDto":{"studentName":"chenlain","studentBirthday":1613474864000,"studentGender":1,"studentCode":"123456","studentEmail":"ibt2015@126.com","schoolCode":null},"schoolDto":null}}

//我的信息-学校
curl http://127.0.0.1:8080/yinniedu/user/getMyInfo  -X GET -H "my_uid:userid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "my_cookie:9e8dd5c8-25e3-41f1-999b-8742896894a2"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"id":"userid1a80eccc-352e-4940-8df0-7a437e972a4a","phoneNum":"18510822965","headerPicUrl":null,"eduRole":2,"studentDto":null,"schoolDto":{"schoolName":"Qinghua University","schoolAddress":"Beijing haidian","schoolCode":"1","schoolEmail":"uhdesk@126.com"}}}

//上传文件
curl http://127.0.0.1:8080/yinniedu/file/upLoadFile   -H "my_uid:useriddb07cd53-962c-4218-a0de-290109ceed20"  -H "my_cookie:a14b088e-333c-471d-9c76-25eb8b3f5cc6"   -F "file=@/Users/ibt/Documents/testdata/yyzz.jpeg"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"url":"http://127.0.0.1:8080/yinniedu/file/readfile/null_2021117_useriddb07cd53-962c-4218-a0de-290109ceed20yyzz.jpeg"}}

//获取文件
curl http://127.0.0.1:8080/yinniedu/file/readfile/null_2021117_useriddb07cd53-962c-4218-a0de-290109ceed20yyzz.jpeg

//发证书
curl http://127.0.0.1:8080/yinniedu/educert/createEduCertificate   -H "my_uid:userid0ea39e47-f000-40d4-aa75-3b874f96cb20"  -H "my_cookie:c1b5997d-1db9-4679-8096-16b2f1ac3052" -X POST -d '{"studentCode":"123456","studentName":"chenlain","certificateUrl":"http://127.0.0.1:8080/yinniedu/readfile/null_2021117_useriddb07cd53-962c-4218-a0de-290109ceed20yyzz.jpeg","note":"good boy","courseItems":[{"courseName":"math","score":"1"}]}' -H "Content-Type: application/json"

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":"educeriduseridcc6a74d8-80a6-416d-8965-2ae96a1b9001"}

//获取某人的证书
curl http://127.0.0.1:8080/yinniedu/educert/getSomeOneEduCertificates  -X POST -H "my_uid:useriddb07cd53-962c-4218-a0de-290109ceed20"  -H "my_cookie:a14b088e-333c-471d-9c76-25eb8b3f5cc6" -H "Content-Type: application/json" -d '{"userId":"useriddb07cd53-962c-4218-a0de-290109ceed20"}'

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":[{"certificateId":"educeriduserid70fde51f-590f-4868-9ab7-b990400d729f","studentCode":"123456","studentName":"chenlain","schoolCode":"1","schoolName":"Qinghua University","txidOnChain":"testTxidOnTlkio","certificateUrl":"http://127.0.0.1:8080/yinniedu/readfile/null_2021117_useriddb07cd53-962c-4218-a0de-290109ceed20yyzz.jpeg","note":"good boy","courseItems":[{"courseName":"math","score":1.0}]}]}

//获取某个证书
curl http://127.0.0.1:8080/yinniedu/educert/getEduCertificate  -X POST -H "my_uid:useriddb07cd53-962c-4218-a0de-290109ceed20"  -H "my_cookie:a14b088e-333c-471d-9c76-25eb8b3f5cc6" -H "Content-Type: application/json" -d '{"certificateId":"educeriduseridcc6a74d8-80a6-416d-8965-2ae96a1b9001"}'

{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":{"certificateId":"educeriduseridcc6a74d8-80a6-416d-8965-2ae96a1b9001","studentCode":"123456","studentName":"chenlain","schoolCode":"1","schoolName":"Qinghua University","txidOnChain":"5ed10d8db565bfda9935df3498896b243ac6b1807c8beaf8c230e37d36925907","certificateUrl":"http://127.0.0.1:8080/yinniedu/readfile/null_2021117_useriddb07cd53-962c-4218-a0de-290109ceed20yyzz.jpeg","note":"good boy","courseItems":[{"courseName":"math","score":1.0}]}}

//获取学校学生
curl http://127.0.0.1:8080/yinniedu/educert/searchStudent   -H "my_uid:userid1a80eccc-352e-4940-8df0-7a437e972a4a"  -H "my_cookie:9e8dd5c8-25e3-41f1-999b-8742896894a2" -X POST -d '{"studentCode":"123456","studentName":"chenlain","phoneNum":"15010913082"}' -H "Content-Type: application/json"
{"code":"0","msg":"成功","enMsg":"success","extraMsg":null,"data":[{"studentId":"useriddb07cd53-962c-4218-a0de-290109ceed20","studentName":"chenlain","studentBirthday":1613474864000,"studentGender":1,"studentCode":"123456","studentEmail":"ibt2015@126.com","phoneNum":"15010913082"}]}




