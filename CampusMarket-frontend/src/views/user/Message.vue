<template>
  <div class="message-container">
    <div class="clear-message">
      <el-tooltip content="标记全部为已读" placement="top">
        <span @click="messageIsRead" class="clear-btn">
          <i class="el-icon-s-open"></i>
        </span>
      </el-tooltip>
    </div>

    <div class="item" v-for="(message, index) in messageList" :key="index">
      <div class="bell" :class="{ unread: !message.isRead, read: message.isRead }">
        <i class="el-icon-message-solid">{{ message.isRead ? '已读' : '未读' }}</i>
      </div>
      <div class="message-content">
        <div class="content">{{ message.content }}</div>
        <div class="time">{{ message.createTime }}</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Message',
  data() {
    return {
      messageList: []
    }
  },
  created() {
    this.fetchMessage();
  },
  methods: {
    messageIsRead() {
      this.$axios.post(`/interaction-api/message/setRead`).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.$notify({
            duration: 1000,
            title: '消息清理成功',
            message: data.msg,
            type: 'success'
          });
          this.fetchMessage();
        }
      }).catch(error => {
        console.log("消息已读设置异常：", error);
      })
    },
    fetchMessage() {
      this.$axios.post(`/interaction-api/message/queryUser`, {}).then(res => {
        const { data } = res; // 解构
        if (data.code === 200) {
          this.messageList = data.data;
        }
      }).catch(error => {
        console.log("查询消息异常：", error);
      })
    },
  }
};
</script>
<style scoped lang="scss">
$message-blue: #2c69f0;
$message-light-blue: #e6f0ff;
$message-dark-blue: #1a3a8f;
$message-gray: #6780b3;

.message-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 10px;

  .clear-message {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 15px;

    .clear-btn {
      width: 36px;
      height: 36px;
      background-color: $message-light-blue;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      color: $message-blue;
      font-size: 18px;
      transition: background-color 0.3s ease;

      &:hover {
        background-color: $message-blue;
        color: white;
      }
    }
  }

  .item {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 16px 12px;
    border-radius: 12px;
    background-color: $message-light-blue;
    box-shadow: 0 2px 8px rgba(44, 105, 240, 0.1);
    margin-bottom: 12px;
    transition: background-color 0.3s ease;

    &:hover {
      background-color: lighten($message-light-blue, 10%);
    }

    .bell {
      width: 52px;
      height: 52px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      font-weight: 600;
      cursor: default;
      transition: all 0.4s ease;
      border: 2px solid $message-blue;

      &.unread {
        background-color: $message-blue;
        color: white;
      }

      &.read {
        background-color: #f0f4ff;
        color: $message-dark-blue;
      }
    }

    .message-content {
      flex-grow: 1;
      display: flex;
      flex-direction: column;

      .content {
        font-size: 16px;
        font-weight: 600;
        color: $message-dark-blue;
        margin-bottom: 6px;
      }

      .time {
        font-size: 12px;
        color: $message-gray;
      }
    }
  }
}
</style>