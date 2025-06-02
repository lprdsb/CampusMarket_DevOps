<template>
  <div class="complaint-page">
    <el-card shadow="always" class="filter-card">
      <el-form :inline="true" size="small" label-width="auto" class="filter-form">
        <el-form-item label="时间范围">
          <!-- <el-date-picker v-model="searchTime" type="daterange" range-separator="至" start-placeholder="开始日期"
            end-placeholder="结束日期" @change="fetchFreshData" style="width: 250px" /> -->
          <el-date-picker v-model="searchTime" type="daterange" range-separator="至" start-placeholder="开始日期"
            end-placeholder="结束日期" style="width: 250px" />
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="hover" class="table-card">
      <el-table :stripe="true" :data="complaints" style="width: 100%">
        <el-table-column prop="id" label="ID" :sortable="true" />
        <el-table-column prop="complainantId" label="投诉人" />
        <el-table-column prop="targetId" label="被投诉人" />
        <el-table-column prop="content" label="内容" />
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span class="status-text">{{ scope.row.status }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="small" style="font-size: large;"
              @click="handle(scope.row.id, 'resolved')">通过</el-button>
            <el-button type="text" size="small" style="font-size: large;"
              @click="handle(scope.row.id, 'rejected')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div class="pagination-wrapper">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
        :page-size="pageSize" :page-sizes="[10, 20]" layout="total, sizes, prev, pager, next, jumper"
        :total="totalItems" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      complaints: [],
      pageSize: 10,
      currentPage: 1,
      totalItems: 0,
      searchTime: [],
    };
  },
  created() {
    this.fetchFreshData();
  },
  methods: {
    fetchFreshData() {
      this.$axios.get('/api/complaint/all').then(res => {
        this.complaints = res.data;
        // console.log(res.data);
      }).catch(error => {
        this.$notify.error({
          title: '查询操作',
          message: error
        });
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchFreshData();
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchFreshData();
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchFreshData();
    },
    handle(id, status) {
      this.$axios.post('/api/complaint/handle', null, { params: { id, status } }).then(res => {
        this.fetchFreshData();
        this.$message.success('处理完成');
      }).catch(error => {
        this.$notify.error({
          title: '修改操作',
          message: error
        });
      });
    }
  }
};
</script>

<style scoped lang="scss">
.complaint-page {
  padding: 10px;
}

.filter-card {
  margin-bottom: 16px;
  border-radius: 10px;

  .el-form-item {
    margin-right: 20px;
  }
}

.pagination-wrapper {
  margin-top: 16px;
  text-align: right;
}

.table-card {
  border-radius: 10px;

  .status-text {
    font-weight: 600;
    color: #ff5722;
  }
}
</style>