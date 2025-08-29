<template>
  <div class="chat-container">
    <!-- 顶部接收者信息栏 -->
    <div class="receiver-info">
      <el-avatar :size="50" :src="receiver.avatar" />
      <div class="receiver-details">
        <h3>{{ receiver.name }}</h3>
        <p>用户ID: {{ receiver.id }}</p>
      </div>
      <el-button type="primary" plain @click="backToProduct">返回商品</el-button>
    </div>

    <!-- 聊天消息区域 -->
    <div class="message-area" ref="messageArea">
      <div v-for="(chatter, index) in chatters" :key="index"
        :class="['message-bubble', chatter.sender === 'me' ? 'sent' : 'received']">
        <div class="message-content">
          <p>{{ chatter.content }}</p>
          <span class="message-time">{{ formatTime(chatter.createTime) }}</span>
        </div>
      </div>
    </div>

    <!-- 消息输入区域 -->
    <div class="input-area">
      <el-input type="textarea" :rows="3" placeholder="输入消息..." v-model="inputMessage" @keyup.enter.native="sendMessage"
        resize="none" />
      <div class="action-buttons">
        <el-button type="primary" @click="sendMessage">发送</el-button>
        <el-button @click="clearInput">清空</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReceiverChat',
  data() {
    return {
      receiver: {
        id: '',
        name: '接收者名称',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        productCount: 0
      },
      inputMessage: '',
      chatters: [],  // 改为 'chatters'，代表聊天记录
      loading: false,
      chatInterval: null  // 用来存储定时器ID，方便清除
    };
  },
  created() {
    this.fetchReceiverInfo();
    this.loadChatHistory();
    // 开始定时拉取聊天记录
    this.startChatPolling();
  },
  destroyed() {
    // 清除定时器，防止内存泄漏
    if (this.chatInterval) {
      clearInterval(this.chatInterval);
    }
  },
  mounted() {
    this.scrollToBottom();
  },
  methods: {
    // 获取接收者的信息
    async fetchReceiverInfo() {
      const receiverId = this.$route.query.ReceiverId || '';
      if (!receiverId) {
        this.$message.error('缺少接收者信息');
        this.$router.back();
        return;
      }

      try {
        const response = await this.$axios.get(`/interaction-api/chat/getById/${receiverId}`);
        const user = response.data.data;

        this.receiver = {
          id: receiverId,
          name: user.userName || '接收者' + receiverId.slice(0, 4),
          avatar: user.userAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        };

      }
      catch (error) {
        console.error('获取接收者信息失败:', error);
        this.$message.error('获取接收者信息失败');
        this.receiver.id = receiverId;
      }
    },

    // 加载聊天记录
    async loadChatHistory() {
      const receiverId = this.$route.query.ReceiverId || '';
      if (!receiverId) return;

      this.loading = true;
      try {
        const chatterQueryDto = {
          senderId: -1,  // 当前用户ID
          receiverId: receiverId,  // 接收者ID
        };
        const response = await this.$axios.post('/interaction-api/chat/queryUser', chatterQueryDto);
        // 将消息处理成 'chatter' 对象
        if (response.data.data && Array.isArray(response.data.data)) {
          this.chatters = response.data.data.map(chatter => {
            // 格式化时间 (根据数据格式)
            const formattedTime = new Date(chatter.createTime.replace('年', '-').replace('月', '-').replace('日', '').replace(' ', 'T'));
            const sender = chatter.senderId == receiverId ? 'receiver' : 'me';
            return {
              sender: sender,  // 判断发送者
              content: chatter.content,
              createTime: formattedTime,  // 格式化时间
            };
          });
        }
        this.chatters.sort((a, b) => a.createTime - b.createTime);
        if (this.chatters.length === 0) {
          this.chatters.push({
            sender: 'receiver',
            content: '您好，您对我的商品有什么问题吗？',
            createTime: new Date(Date.now() - 3600000)
          });
        }
      }
      catch (error) {
        console.error('加载聊天记录失败:', error);
      }
      finally {
        this.loading = false;
      }
    },

    // 定时拉取最新的聊天记录
    startChatPolling() {
      this.chatInterval = setInterval(() => {
        this.loadChatHistory();
      }, 500);  // 每1秒钟请求一次最新的聊天记录
    },

    // 发送消息
    async sendMessage() {
      const receiverId = this.$route.query.ReceiverId || '';
      if (!this.inputMessage.trim()) return;
      if (!receiverId) {
        this.$message.error('无法确定接收方');
        return;
      }


      try {
        // 发送聊天消息
        await this.$axios.post('/interaction-api/chat/send', {
          receiverId: receiverId,
          content: this.inputMessage,
          isRead: 1
        });
        const newMessage = {
          sender: 'me',
          content: this.inputMessage,
          receiver: 'receiver'
        };
        this.chatters.push(newMessage);
        this.inputMessage = '';  // 清空输入框
        this.scrollToBottom();

        setTimeout(() => {
          this.simulateReceiverReply();
        }, 1500);

      } catch (error) {
        console.error('发送消息失败:', error);
        this.$message.error('发送消息失败');
      }
    },

    // 清空输入框
    clearInput() {
      this.inputMessage = '';
    },

    // 返回商品页面
    backToProduct() {
      this.$router.back();
    },

    // 格式化时间
    formatTime(time) {
      if (time === null) {
        return null;
      }
      return time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messageArea;
        container.scrollTop = container.scrollHeight;
      });
    }
  }
};
</script>

<style scoped lang="scss">
$blue-primary: #2c69f0;
$blue-light: #e6f0ff;
$blue-lighter: #f0f6ff;
$text-primary: #1a3a8f;
$text-muted: #6780b3;
$sent-bg: #2c69f0;
$received-bg: #ffffff;

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 20px auto;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 18px rgba(44, 105, 240, 0.25);
  background-color: $blue-lighter;
}

.receiver-info {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  background-color: $blue-primary;
  color: white;

  el-avatar {
    border: 2px solid white;
  }

  .receiver-details {
    flex: 1;
    margin-left: 16px;

    h3 {
      margin: 0;
      font-size: 20px;
      font-weight: 700;
    }

    p {
      margin: 4px 0 0;
      font-size: 14px;
      opacity: 0.85;
    }
  }

  .el-button {
    border-color: 1e90ff;
    color: 1e90ff;
    transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;

    &:hover,
    &:focus {
      background-color: rgba(30, 144, 255, 0.15);
      /* 浅蓝色半透明 */
      color: #1e90ff;
      /* 道奇蓝 */
      border-color: #1e90ff;
    }
  }
}

.message-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: $blue-light;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message-bubble {
  display: flex;
  max-width: 70%;
  word-wrap: break-word;

  &.sent {
    align-self: flex-end;
    justify-content: flex-end;

    .message-content {
      background-color: $sent-bg;
      color: white;
      border-radius: 18px 18px 4px 18px;
      box-shadow: 0 4px 8px rgba(44, 105, 240, 0.3);
      padding: 10px 16px;
    }

    .message-time {
      color: rgba(255, 255, 255, 0.75);
      text-align: right;
      margin-top: 6px;
      font-size: 11px;
    }
  }

  &.received {
    align-self: flex-start;

    .message-content {
      background-color: $received-bg;
      color: $text-primary;
      border: 1px solid $blue-primary;
      border-radius: 18px 18px 18px 4px;
      padding: 10px 16px;
      box-shadow: 0 2px 6px rgba(44, 105, 240, 0.15);
    }

    .message-time {
      color: $text-muted;
      text-align: left;
      margin-top: 6px;
      font-size: 11px;
    }
  }
}

.input-area {
  padding: 16px 20px;
  background-color: white;
  border-top: 1px solid $blue-light;

  .el-input {
    width: 100%;
    font-size: 14px;

    textarea {
      border-radius: 10px;
      border: 1px solid $blue-primary !important;
      resize: none;
      padding: 10px;
      font-family: inherit;
      font-size: 14px;
      transition: border-color 0.3s ease;

      &:focus {
        border-color: darken($blue-primary, 10%) !important;
        box-shadow: 0 0 6px rgba(44, 105, 240, 0.5);
      }
    }
  }

  .action-buttons {
    margin-top: 10px;
    text-align: right;

    .el-button {
      min-width: 100px;
      /* 你觉得合适的宽度 */
      padding: 8px 16px;
      margin-left: 10px;
      box-sizing: border-box;
    }
  }
}
</style>