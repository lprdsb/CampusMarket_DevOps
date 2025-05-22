<template>
  <div class="chat-container">
    <!-- 顶部接收者信息栏 -->
    <div class="receiver-info">
      <el-avatar :size="50" :src="receiver.avatar"></el-avatar>
      <div class="receiver-details">
        <h3>{{ receiver.name }}</h3>
        <p>用户ID: {{ receiver.id }}</p>
      </div>
      <el-button type="info" plain @click="backToProduct">返回商品</el-button>
    </div>

    <!-- 聊天消息区域 -->
    <div class="message-area" ref="messageArea">
      <div
          v-for="(chatter, index) in chatters"
          :key="index"
          :class="['message-bubble', chatter.sender === 'me' ? 'sent' : 'received']"
      >
        <div class="message-content">
          <p>{{ chatter.content }}</p>
          <span class="message-time">{{ formatTime(chatter.createTime) }}</span>
        </div>
      </div>
    </div>

    <!-- 消息输入区域 -->
    <div class="input-area">
      <el-input
          type="textarea"
          :rows="3"
          placeholder="输入消息..."
          v-model="inputMessage"
          @keyup.enter.native="sendMessage"
          resize="none"
      ></el-input>
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
      const receiverId = this.$route.query.ReceiverId||'';
      if (!receiverId) {
        this.$message.error('缺少接收者信息');
        this.$router.back();
        return;
      }

      try {
        const response = await this.$axios.get(`/chat/getById/${receiverId}`);
        console.log(response.data);
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
      const receiverId = this.$route.query.ReceiverId||'';
      if (!receiverId) return;

      this.loading = true;
      //try {
        const chatterQueryDto = {
          senderId: -1,  // 当前用户ID
          receiverId: receiverId,  // 接收者ID
        };
        const response = await this.$axios.post('/chat/queryUser',chatterQueryDto);

        // 将消息处理成 'chatter' 对象
        this.chatters = response.data.map(chatter => ({
          sender: chatter.senderId === this.$store.state.user.id ? 'me' : 'receiver', // 修改接收者为 'receiver'
          content: chatter.content,
          createTime: new Date(chatter.createTime)
        }));

        if (this.chatters.length === 0) {
          this.chatters.push({
            sender: 'receiver',
            content: '您好，您对我的商品有什么问题吗？',
            createTime: new Date(Date.now() - 3600000)
          });
        }
      //}
      /*catch (error)
      {
        console.error('加载聊天记录失败:', error);
        this.$message.error('加载聊天记录失败');
      }
      finally
      {
        this.loading = false;
      }*/
    },

    // 定时拉取最新的聊天记录
    startChatPolling() {
      this.chatInterval = setInterval(() => {
        this.loadChatHistory();
      }, 1000);  // 每1秒钟请求一次最新的聊天记录
    },

    // 发送消息
    async sendMessage() {
      const receiverId = this.$route.query.ReceiverId||'';
      if (!this.inputMessage.trim()) return;
      if (!receiverId) {
        this.$message.error('无法确定接收方');
        return;
      }


      try {
        // 发送聊天消息
        await this.$axios.post('/chat/send', {
          receiverId: receiverId,
          content: this.inputMessage,
          isRead:1
        });
        const newMessage = {
          sender: '我',
          content: this.inputMessage,
          receiver:'对方'
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
      return time.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
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

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 800px;
  margin: 0 auto;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.receiver-info {
  display: flex;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #ebeef5;
  background-color: #f5f7fa;
}

.receiver-details {
  flex: 1;
  margin-left: 15px;
}

.receiver-details h3 {
  margin: 0;
  font-size: 18px;
}

.receiver-details p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #909399;
}

.message-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f9f9f9;
}

.message-bubble {
  margin-bottom: 15px;
  display: flex;
}

.message-bubble.sent {
  justify-content: flex-end;
}

.message-bubble.received {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 10px 15px;
  border-radius: 18px;
  position: relative;
}

.sent .message-content {
  background-color: #409eff;
  color: white;
}

.received .message-content {
  background-color: white;
  border: 1px solid #ebeef5;
}

.message-time {
  display: block;
  font-size: 12px;
  margin-top: 5px;
  opacity: 0.8;
}

.sent .message-time {
  text-align: right;
  color: rgba(255, 255, 255, 0.8);
}

.received .message-time {
  text-align: left;
  color: #909399;
}

.input-area {
  padding: 15px;
  border-top: 1px solid #ebeef5;
  background-color: white;
}

.action-buttons {
  margin-top: 10px;
  text-align: right;
}
</style>
