<template>
    <div class="container1">
        <!-- 头像 -->
        <!-- <div class="info">
            <img :src="userInfo.userAvatar" alt="" srcset="">
            <span>{{ userInfo.userName }}</span>
        </div> -->
        <el-row :span="20" style="height: 50px;">
            <el-col @click.native="route()">
                <div class="info">
                    <img :src="userInfo.userAvatar" alt="" srcset="">
                    <span>{{ userInfo.userName }}</span>
                </div>
            </el-col>
        </el-row>
        <!-- <br>
        <el-row :span="20" style="height: 50px;">
            <div class="info">
                <img :src="userInfo.userAvatar" alt="" srcset="">
                <span>{{ userInfo.userName }}</span>
            </div>
        </el-row> -->
    </div>
</template>
<script>
export default {
    name: "Star",
    data() {
        return {
            userInfo: {},
            userAvatar: '',
        }
    },
    created() {
        this.auth();
    },
    methods: {

        route() {
            // 跳转商品详情
            // console.log(this.userInfo.id);
            this.$router.push('/space?userId=' + this.userInfo.id);
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
        async auth() {
            const { data } = await this.$axios.get('/user/auth');
            if (data.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                this.userAvatar = data.data.userAvatar;
            }
        },
    }
};
</script>
<style scoped lang="scss">
.container {
    width: 500px;
    margin: 0 auto;
    padding: 0;
}

.info {
    display: flex;
    justify-content: left;
    align-items: center;
    gap: 10px;

    img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }

    span {
        font-size: 20px;
        color: #999;
    }
}
</style>
