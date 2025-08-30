<script setup>
import { ref } from 'vue'
const userPasswordData = ref({
    old_pwd:'',
    new_pwd:'',
    re_pwd:''
})
const rules = {
    old_pwd: [
        { required: true, message: '请输入原密码', trigger: 'blur' },
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
    ],
    re_pwd:[
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
    ],
}
import {userPasswordUpdateService} from '@/api/user.js'
import { ElMessage,ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
const router = useRouter();
//更新用户密码
const updateUserPassword= ()=>{
    
    ElMessageBox.confirm(
            '你确认要修改密码吗',
            '温馨提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(async () => {
                //确认被点击
                //调用接口
                userPasswordUpdateService(userPasswordData.value);
                //2.跳转到登录页面
                router.push('/login')
                ElMessage({
                    type: 'success',
                    message: '修改密码成功，请重新登录',
                })
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '取消修改密码',
                })
            })
}



</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>重置密码
                </span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userPasswordData" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="原密码" prop="old_pwd">
                        <el-input v-model="userPasswordData.old_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="userPasswordData.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="re_pwd">
                        <el-input v-model="userPasswordData.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserPassword">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>