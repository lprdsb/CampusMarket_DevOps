<template>
  <el-table :data="complaints">
    <el-table-column prop="id" label="ID" />
    <el-table-column prop="complainantId" label="投诉人" />
    <el-table-column prop="targetId" label="被投诉人" />
    <el-table-column prop="content" label="内容" />
    <el-table-column prop="status" label="状态" />
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button v-if="scope.row.status==='pending'" @click="handle(scope.row.id, 'resolved')">通过</el-button>
        <el-button v-if="scope.row.status==='pending'" @click="handle(scope.row.id, 'rejected')">驳回</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return { complaints: [] };
  },
  created() {
    this.fetchComplaints();
  },
  methods: {
    async fetchComplaints() {
      const res = await axios.get('/api/complaint/all');
      this.complaints = res.data;
    },
    async handle(id, status) {
      await axios.post('/api/complaint/handle', null, { params: { id, status } });
      this.fetchComplaints();
      this.$message.success('处理完成');
    }
  }
};
</script>