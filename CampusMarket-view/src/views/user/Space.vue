<template>
    <!-- 头像 -->
    <div style="display: flex; gap: 100px;">
        <div>
            <div>
                <p>头像</p>
                <img v-if="userAvatar" :src="userAvatar" style="width: 88px;height: 88px;">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </div>
            <!-- 昵称 -->
            <div>
                <p>昵称</p>
                <div class="text" style="font-size: 15px;">
                    {{ userInfo.userName }}
                </div>
            </div>
            <!-- 邮箱 -->
            <div>
                <p>邮箱</p>
                <div class="text" style="font-size: 15px;">
                    {{ userInfo.userEmail }}
                </div>
            </div>
            <!-- 账号状态 -->
            <div>
                <div>
                    <p style="font-size: 16px;">
                        账号状态
                        <el-tooltip class="item" effect="dark" content="一经封号，不可登录，不可使用系统功能" placement="right">
                            <i class="el-icon-info"></i>
                        </el-tooltip>
                    </p>
                    <div>
                        <span v-if="!userInfo.isLogin" style="font-size: 14px;color: rgb(27, 156, 53);"><i
                                style="margin-right: 6px;" class="el-icon-circle-check"></i>正常</span>
                        <span v-else
                            style="font-size: 14px;color: rgb(230, 63, 49);text-decoration: underline;text-decoration-style: dashed;">
                            <i style="margin-right: 6px;" class="el-icon-warning-outline"></i>已被封禁</span>
                    </div>
                </div>
                <div>
                    <p style="font-size: 16px;">
                        留言状态
                        <el-tooltip class="item" effect="dark" content="禁言后，互动功能受限" placement="right">
                            <i class="el-icon-info"></i>
                        </el-tooltip>
                    </p>
                    <div>
                        <span v-if="!userInfo.isWord" style="font-size: 14px;color: rgb(27, 156, 53);">
                            <i style="margin-right: 6px;" class="el-icon-circle-check"></i>正常</span>
                        <span v-else
                            style="font-size: 14px;color: rgb(230, 63, 49);text-decoration: underline;text-decoration-style: dashed;">
                            <i style="margin-right: 6px;" class="el-icon-warning-outline"></i>已被封禁</span>
                    </div>
                    <div v-if="!userInfo.isLogin && selfInfo.id === userInfo.id"
                        style="margin-top: 20px;text-align: left;">
                        <el-button type="primary" class="customer" size="mini" @click="handleRouteSelect('/myself')"
                            round>编辑资料
                        </el-button>
                    </div>
                </div>
            </div>
        </div>
        <div style="width: 10000px;">
            <!-- <p>{{ userInfo.id }}</p> -->
            <ProductList v-if="userInfo.id" :userId=userInfo.id />
        </div>
    </div>


</template>
<script>
import ProductList from '@/views/user/ProductList'
export default {
    components: { ProductList },
    name: "Space",
    data() {
        return {
            selfInfo: {},
            userInfo: {},
            userAvatar: '',
        }
    },
    created() {
        // this.auth();
        this.getParam();
    },
    methods: {
        // async auth() {
        //     const { data } = await this.$axios.get('/user/auth');
        //     if (data.code !== 200) { // Token校验异常
        //         this.$router.push('/');
        //     } else {
        //         this.userInfo = data.data;
        //         this.userAvatar = data.data.userAvatar;
        //     }
        // },
        getParam() {
            const param = this.$route.query;
            this.userId = Number(param.userId);
            // console.log(this.userId);
            this.fetchUser(this.userId);
        },
        async fetchUser(userId) {
            const { data } = await this.$axios.get('/user/getById/' + userId);
            if (data.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                this.userInfo = data.data;
                this.userAvatar = data.data.userAvatar;
            }
            const { data: authData } = await this.$axios.get('/user/auth');
            if (authData.code !== 200) { // Token校验异常
                this.$router.push('/');
            } else {
                this.selfInfo = authData.data;
            }
            // console.log(this.selfInfo.id == this.userInfo.id);
        },
        handleRouteSelect(path) {
            if (this.$router.currentRoute.fullPath !== path) {
                this.$router.push(path);
            }
        },
    }
};
</script>
<style scoped lang="scss">
.container {
    display: flex;
    justify-content: left;
    width: 1000px;
    height: 1000px;
}


.text {
    justify-content: left;
    align-items: center;
    text-align: left;
    gap: 8px;
    margin-top: 15px;
    font-size: 30px;
    font-weight: 800;
}
</style>
