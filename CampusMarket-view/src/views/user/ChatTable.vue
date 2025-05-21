<template>
  <div class="chat-list">
    <div class="clear-message">
      <span @click="clearChats">
        <i class="el-icon-s-open"></i>
      </span>
    </div>
    <el-row v-if="chatList.length === 0">
      <el-empty description="暂无聊天记录"></el-empty>
    </el-row>
    <el-row v-else>
      <el-col :span="24" v-for="(chat, index) in chatList" :key="index">
        <div class="chat-item" @click="enterChat(chat)">
          <div class="avatar">
            <img :src="chat.avatar || defaultAvatar" alt="用户头像">
          </div>
          <div class="chat-info">
            <div class="chat-header">
              <span class="username">{{ chat.username }}</span>
              <span class="time">{{ formatTime(chat.lastTime) }}</span>
            </div>
            <div class="preview">
              {{ chat.lastMessage || '暂无消息' }}
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'ChatList',
  data() {
    return {
      chatList: [],
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    };
  },
  created() {
    this.fetchChatList();
  },
  methods: {
    clearChats() {
      this.$axios.post('/chat/clearAll').then(res => {
        const { data } = res;
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '清除成功',
            message: '已清空所有聊天记录',
            type: 'success'
          });
          this.fetchChatList();
        }
      }).catch(error => {
        console.log("清除聊天记录异常：", error);
      })
    },
    enterChat(chat) {
      // 跳转到具体聊天界面
      window.location.href = `http://localhost:21091/chat?SellerId=${chat.userId}`;
    },
    formatTime(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      return `${date.getMonth()+1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    },
    fetchChatList() {
      this.$axios.get('/chat/list').then(res => {
        const { data } = res;
        if (data.code === 200) {
          this.chatList = data.data;
        }
      }).catch(error => {
        console.log("获取聊天列表异常：", error);
      })
    },
  }
};
</script>

<style scoped lang="scss">
.chat-list {
  padding: 20px;

  .clear-message {
    margin: 10px 0;
    display: flex;
    justify-content: right;

    span:hover {
      background-color: rgb(241, 241, 241);
    }

    span {
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      background-color: rgb(246, 246, 246);
    }
  }

  .chat-item {
    display: flex;
    padding: 12px;
    margin-bottom: 8px;
    border-radius: 8px;
    transition: all 0.3s;
    cursor: pointer;
    align-items: center;

    &:hover {
      background-color: #f5f5f5;
    }

    .avatar {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      overflow: hidden;
      margin-right: 12px;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .chat-info {
      flex: 1;
      min-width: 0;

      .chat-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 4px;

        .username {
          font-size: 16px;
          font-weight: 500;
          color: #333;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .time {
          font-size: 12px;
          color: #999;
        }
      }

      .preview {
        font-size: 14px;
        color: #666;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}
</style>