<template>
    <div class="user-space">
      <!-- 左侧信息面板 -->
      <div class="user-info-panel">
        <!-- 头像 -->
        <div class="avatar-wrapper">
          <img v-if="userAvatar" :src="userAvatar" class="avatar-img" />
          <i v-else class="el-icon-plus avatar-placeholder"></i>
        </div>
  
        <!-- 关注按钮 -->
        <div v-if="selfInfo.id && selfInfo.id !== userInfo.id" class="follow-btn">
          <el-button
            :type="starFlag ? 'danger' : 'primary'"
            @click="toggleStar"
            size="medium"
            style="height: 40px; width: 120px; font-size: 20px;"
            round>
            {{ starFlag ? '取消关注' : '关注TA' }}
          </el-button>
        </div>
  
        <!-- 昵称 -->
        <div class="info-item">
          <p class="label">昵称</p>
          <div class="value">{{ userInfo.userName }}</div>
        </div>
  
        <!-- 邮箱 -->
        <div class="info-item">
          <p class="label">邮箱</p>
          <div class="value">{{ userInfo.userEmail }}</div>
        </div>
  
        <!-- 账号状态 -->
        <div class="info-item">
          <p class="label">
            账号状态
            <el-tooltip effect="dark" content="一经封号，不可登录，不可使用系统功能" placement="right">
              <i class="el-icon-info"></i>
            </el-tooltip>
          </p>
          <div :class="['status-tag', !userInfo.isLogin ? 'normal' : 'banned']">
            <i :class="!userInfo.isLogin ? 'el-icon-circle-check' : 'el-icon-warning-outline'"></i>
            {{ !userInfo.isLogin ? '正常' : '已被封禁' }}
          </div>
        </div>
  
        <!-- 留言状态 -->
        <div class="info-item">
          <p class="label">
            留言状态
            <el-tooltip effect="dark" content="禁言后，互动功能受限" placement="right">
              <i class="el-icon-info"></i>
            </el-tooltip>
          </p>
          <div :class="['status-tag', !userInfo.isWord ? 'normal' : 'banned']">
            <i :class="!userInfo.isWord ? 'el-icon-circle-check' : 'el-icon-warning-outline'"></i>
            {{ !userInfo.isWord ? '正常' : '已被封禁' }}
          </div>
        </div>
  
        <!-- 编辑按钮 -->
        <div v-if="!userInfo.isLogin && selfInfo.id === userInfo.id" class="edit-btn">
          <el-button type="primary" size="mini" round @click="handleRouteSelect('/myself')">
            编辑资料
          </el-button>
        </div>
      </div>
  
      <!-- 右侧商品列表 -->
      <div class="product-list">
        <ProductList v-if="userInfo.id" :userId="userInfo.id" />
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
            starFlag: false,
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
            this.auth();
        },
        auth() {
            this.$axios.get('/user/auth').then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.selfInfo = data.data;
                    this.fetchUser();
                }
            }).catch(error => {
                this.$notify.error({
                    title: '查询异常',
                    message: error
                });
            });
        },
        fetchUser() {
            this.$axios.get('/user/getById/' + this.userId).then(res => {
                const { data } = res;
                if (data.code !== 200) { // Token校验异常
                    this.$router.push('/');
                } else {
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
        fetchStar() {
            const starQueryDto = {
                user1Id: this.selfInfo.id,
                user2Id: this.userInfo.id
            };
            this.$axios.post('/star/query', starQueryDto).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    // 代表没有收藏
                    this.starFlag = data.total !== 0;
                }
            }).catch(error => {
                console.log("商品查询异常：", error);
            })
        },
        handleRouteSelect(path) {
            if (this.$router.currentRoute.fullPath !== path) {
                this.$router.push(path);
            }
        },
        toggleStar() {
            // console.log(this.starFlag);
            this.$axios.post(`/star/starOperation/${this.userInfo.id}`).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.starFlag = data.data;
                    this.$notify({
                        duration: 1000,
                        title: '关注操作成功',
                        message: data.msg,
                        type: 'success'
                    });
                }
            }).catch(error => {
                console.log("关注异常：", error);
            })
        },
    }
};
</script>
<style scoped lang="scss">
.user-space {
  display: flex;
  gap: 60px;
  padding: 30px;
}

.user-info-panel {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.avatar-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.avatar-placeholder {
  font-size: 50px;
  color: #ccc;
}

.follow-btn {
  text-align: center;
}

.info-item {
  .label {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 4px;
    color: #666;
  }

  .value {
    font-size: 16px;
    font-weight: 500;
    background-color: #f8f8f8;
    padding: 6px 10px;
    border-radius: 6px;
  }
}

.status-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 500;
  padding: 4px 10px;
  border-radius: 12px;

  &.normal {
    color: #1b9c35;
    background-color: #e6f4ea;
  }

  &.banned {
    color: #e63f31;
    background-color: #fcebea;
    text-decoration: underline dashed;
  }
}

.edit-btn {
  margin-top: 10px;
}
.product-list {
  flex: 1;
}
</style>
