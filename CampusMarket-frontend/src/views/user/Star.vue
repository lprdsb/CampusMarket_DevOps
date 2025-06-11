<template>
    <div class="star-container">
        <div v-if="userList.length === 0" class="empty">

            <el-empty description="暂无关注用户"></el-empty>
        </div>
        <el-row :gutter="20">
            <el-col :span="24" v-for="(id, index) in userList" :key="index">
                <div class="user-card" @click="route('/space?userId=' + id.id)">
                    <img :src="id.userAvatar" alt="" srcset="">
                    <div class="user-name">{{ id.userName }}</div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
export default {
    name: "Star",
    data() {
        return {
            userInfo: {},
            userAvatar: '',
            userList: [],
        }
    },

    created() {
        this.auth();
    },
    methods: {

        route(path) {
            // 跳转关注列表
            // console.log(this.userInfo.id);
            this.$router.push(path);
        },

        fetchStar() {
            this.$axios.post('/star/queryByUser1/' + this.userInfo.id, {}).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.userList = data.data;
                }
            }).catch(error => {
                console.log("关注异常：", error);
            })
        },

        // 提交个人信息修改
        async postInfo() {
            const { userName, userEmail } = this.userInfo;
            const userUpdateDTO = {
                userAvatar: this.userAvatar,
                userName,
                userEmail
            }
            const { data } = await this.$axios.put('/user/update', userUpdateDTO);
            this.$notify({
                position: 'buttom-right',
                duration: 1000,
                title: '编辑个人信息',
                message: data.code === 200 ? '编辑成功' : '编辑失败',
                type: data.code === 200 ? 'success' : 'error'
            });
            // 返回首页
            if (data.code === 200) {
                this.auth();
            }
        },
        // 头像上传
        handleAvatarSuccess(res, file) {
            this.$notify({
                duration: 1500,
                title: '头像上传',
                message: res.code === 200 ? '上传成功' : '上传失败',
                type: res.code === 200 ? 'success' : 'error'
            });
            // 上传成功则更新用户头像
            if (res.code === 200) {
                this.userAvatar = res.data;
            }
        },
        // Token 检验 ,取得用户信息
        auth() {

            this.$axios.get('/user/auth').then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.userInfo = data.data;
                    this.userAvatar = data.data.userAvatar;
                    this.fetchStar();
                }
            }).catch(error => {
                this.$notify.error({
                    title: '查询异常',
                    message: error
                });
            });
        },
    }
};
</script>
<style scoped lang="scss">
.star-container {
    max-width: 600px;
    margin: 30px auto;
    padding: 20px;

    .title {
        font-size: 24px;
        font-weight: 700;
        text-align: center;
        margin-bottom: 30px;
        color: #333;
    }

    .empty {
        text-align: center;
        font-size: 16px;
        color: #aaa;
        margin-top: 20px;
    }

    .user-card {
        display: flex;
        align-items: center;
        gap: 20px;
        padding: 12px 20px;
        margin-bottom: 15px;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        transition: all 0.3s ease;
        cursor: pointer;

        &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
        }

        img {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            border: 2px solid #f2f2f2;
            object-fit: cover;
        }

        .user-name {
            font-size: 18px;
            font-weight: 600;
            color: #333;
        }
    }
}
</style>
