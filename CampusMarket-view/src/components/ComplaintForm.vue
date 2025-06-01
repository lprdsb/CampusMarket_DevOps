<template>
  <el-dialog :visible.sync="visible" title="投诉">
    <el-form @submit.native.prevent="submit">
      <el-form-item label="投诉内容">
        <el-input type="textarea" v-model="content" required />
      </el-form-item>
      <el-button type="primary" @click="submit">提交</el-button>
    </el-form>
  </el-dialog>
</template>

<script>
import axios from 'axios';
export default {
  props: ['visible', 'targetId', 'orderId', 'complainantId'],
  data() {
    return { content: '' };
  },
  methods: {
    async submit() {
      await axios.post('/api/complaint/submit', {
        complainantId: this.complainantId,
        targetId: this.targetId,
        orderId: this.orderId,
        content: this.content
      });
      this.$emit('update:visible', false);
      this.$message.success('投诉已提交');
    }
  }
};
</script>