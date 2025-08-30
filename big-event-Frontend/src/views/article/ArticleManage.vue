<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//文章分类数据模型
const categorys = ref([
    {
        "id": 3,
        "categoryName": "美食",
        "categoryAlias": "my",
        "createTime": "2023-09-02 12:06:59",
        "updateTime": "2023-09-02 12:06:59"
    },
    {
        "id": 4,
        "categoryName": "娱乐",
        "categoryAlias": "yl",
        "createTime": "2023-09-02 12:08:16",
        "updateTime": "2023-09-02 12:08:16"
    },
    {
        "id": 5,
        "categoryName": "军事",
        "categoryAlias": "js",
        "createTime": "2023-09-02 12:08:33",
        "updateTime": "2023-09-02 12:08:33"
    }
])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
    {
        "id": 5,
        "title": "陕西旅游攻略",
        "content": "兵马俑,华清池,法门寺,华山...爱去哪去哪...",
        "coverImg": "https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png",
        "state": "草稿",
        "categoryId": 2,
        "createTime": "2023-09-03 11:55:30",
        "updateTime": "2023-09-03 11:55:30"
    },
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}
//回显文章的分类到搜索的下拉选择框绑定的数据模型中
import { articleCategoryListService } from '@/api/article.js'
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categorys.value = result.data
}
articleCategoryList()

import { articleListService } from '@/api/article.js'
//获取文章列表数据
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params);
    total.value = result.data.total
    articles.value = result.data.items
    //处理数据，给数据模型拓展一个categoryName，分类名称
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId == categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}
articleList();
//导入Quill组件和样式
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { Plus } from '@element-plus/icons-vue'
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})
//导入token
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
//上传成功的回调函数
const uploadSuccess = (result) => {
    articleModel.value.coverImg = result.data;
    console.log(result.data);
}

import { ElMessage } from 'element-plus'
import { articleAddService } from '@/api/article.js'
//添加文章
const addArticle = async (clickState) => {

    //把发布状态赋值给数据模型
    articleModel.value.state = clickState
    //调用接口
    let result = await articleAddService(articleModel.value);
    //提示
    ElMessage.success(result.message ? result.message : '添加成功')
    //关闭抽屉
    visibleDrawer.value = false
    //刷新当前列表
    articleList()

}


// 获取编辑器实例
const quillRef = ref(null);
const clearData = () => {
    // 1. 重置绑定的变量
    articleModel.value = {
        title: '',
        categoryId: '',
        coverImg: '',
        content: '',
        state: ''
    };

    // 2. 正确获取 Quill 实例并清空内容
    if (quillRef.value) {
        // 关键：通过 getQuill() 方法获取实例
        const quill = quillRef.value.getQuill();
        // 清空编辑器内容（两种方法均可）
        quill.setText(''); // 推荐：直接设置为空文本
        // 或 quill.setContents([]); // 清空所有内容（包括格式）
    }
};

//定义变量，控制标题的展示
const title = ref('')
//展示编辑弹窗
const showDialog = (row) => {
    visibleDrawer.value = true; title.value = '修改文章'
    //数据拷贝
    articleModel.value.title = row.title;
    articleModel.value.content = row.content;
    articleModel.value.coverImg = row.coverImg;
    articleModel.value.categoryId = row.categoryId
    //拓展id属性，将来需要传递给后台，完成分类的修改
    articleModel.value.id = row.id
}

import { articleUpdateService } from '@/api/article.js'
const updateArticle = async (clickState) => {
    articleModel.value.state = clickState
    let result = await articleUpdateService(articleModel.value);
    //提示
    ElMessage.success(result.message ? result.message : '修改成功')
    //关闭抽屉
    visibleDrawer.value = false
    //刷新当前列表
    articleList()
}

import { ElMessageBox } from 'element-plus'
import {articleDeleteService} from '@/api/article.js'
//删除文章函数
const deleteArticle =async(row)=>{
    //提示用户，弹窗确认框
    ElMessageBox.confirm(
        '你确认要删除该文章吗',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async() => {
            //确认被点击
            //调用接口
            let result = await articleDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //调用获取文章的函数，刷新列表
            articleList()
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
}

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true; title = '添加文章'; clearData()">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 200px">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state" style="width: 200px">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="categoryId = ''; state = ''; articleList()">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        <!-- 抽屉 -->
        <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">
                    <!-- 
                    将来当点击+图标，选择本地图片后，el-upload这个组件会自动发送请求，把图片上传到指定的服务器上，
                    而不需要我们自己使用axios发送异步请求，所以需要给el-upload标签添加一些属性，控制请求的发送

                    auto-upload:是否自动上传
                    action: 服务器接口路径
                    name: 上传的文件字段名
                    headers: 设置上传的请求头
                    on-success: 上传成功的回调函数
                     -->
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false" action="/api/upload"
                        name="file" :headers="{ 'Authorization': tokenStore.token }" :on-success="uploadSuccess">
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <!-- 富文本编辑器 -->
                    <div class="editor">
                        <quill-editor theme="snow" ref="quillRef" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary"
                        @click="title == '添加文章' ? addArticle('已发布') : updateArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="title == '添加文章' ? addArticle('草稿') : updateArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>