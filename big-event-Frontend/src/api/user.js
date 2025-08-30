//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用注册接口的函数
export const userRegisterService=(registerData)=>{
    //借助于UrlSearchParams完成传递,不使用的话传递的是json数据，不符合接口文档请求参数的格式
    const params = new URLSearchParams();
    //遍历registerData把数据传入params转化成需要的格式
    for(let key in registerData){
        params.append(key,registerData[key]);
    }
    //利用request.js工具发起axios请求，携带参数
    return request.post('/user/register',params)
}

//提供调用登录接口的函数
export const userLoginService = (loginData) =>{
    //借助于UrlSearchParams完成传递,不使用的话传递的是json数据，不符合接口文档请求参数的格式
    const params = new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key]);
    }
    return request.post('/user/login',params)
}

//获取用户详细信息
export const userInfoService =()=>{
    return request.get('/user/userInfo')
}

//修改用户基本信息
export const userInfoUpdateService=(userInfoData)=>{
    return request.put('/user/update',userInfoData)
}
//修改头像
export const userAvatarUpdateService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/user/updateAvatar',params)
}
//修改密码
export const userPasswordUpdateService = (userPasswordData)=>{
    return request.patch('/user/updatePwd',userPasswordData)
}