//定制请求的实例

//导入axios  npm install axios

import axios from 'axios';
import { ElMessage } from 'element-plus'

//定义一个变量,记录公共的前缀  ,  baseURL
//const baseURL = 'http://localhost:8080';
//不直接请求后端，路径中段改为/api，让配置文件vite.config.js获取到
const baseURL = '/api';
const instance = axios.create({baseURL})

import {useTokenStore} from '@/stores/token.js'
//添加请求拦截器，统一添加token
//向后端发送请求的时候拦截检查，token是否存在，如果存在就携带token到请求头上再发起请求
instance.interceptors.request.use(
    (config)=>{
        //请求前的回调
        //添加token
        const tokenStore = useTokenStore();
        // 判断有没有token
        // 如果有token
        if(tokenStore.token){
            config.headers.Authorization = tokenStore.token
        }
        return config;
    },
    (err)=>{
        // 请求错误的回调
        //异步的状态转化成失败的状态
         Promise.reject(err);
    }


)


import router from '@/router'

//添加响应拦截器
//注意这里的result.data不是指的里面的数据，而是整个result被读取，如果要里面的数据的话，需要使用result.data.code这个样的格式
instance.interceptors.response.use(
    //接口调用成功
    result=>{
        //判断业务状态码，接口中的操作成功
        if(result.data.code ===0){
            return result.data;
        }
        //操作失败
        //alert(result.data.message?result.data.message:'服务异常');
        ElMessage.error(result.data.message?result.data.message:'服务异常')
        //异步操作的状态转为失败
        return Promise.reject(result.data);
    },
    //接口调用失败
    err=>{
        //判断响应状态码，如果为401，则证明未登录，并跳转到登录页面
        if(err.response.status===401){
            ElMessage.error('请先登录')
            router.push('/login')
        }else{
            ElMessage.error('服务异常')
        }
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;