<template>
    <div class="category-page">
        <el-card shadow="always" class="filter-card">
            <el-form :inline="true" size="small" label-width="auto" class="filter-form">
                <el-form-item label="类别名">
                    <el-input size="small" style="width: 166px;" v-model="categoryQueryDto.name" placeholder="商品类别名"
                        clearable @clear="handleFilterClear">
                        <el-button slot="append" @click="handleFilter" icon="el-icon-search"
                            style="background-color: #4a90e2;"></el-button>
                    </el-input>
                </el-form-item>
                <el-form-item style="float: right; ">
                    <span class="edit-button" style="background-color: #4a90e2;" @click="add()">
                        新增类别
                    </span>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card shadow="hover" class="table-card">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="id" width="88" label="ID" :sortable="true"></el-table-column>
                <el-table-column prop="name" label="类别名"></el-table-column>
                <el-table-column label="操作" width="120">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-wrapper">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20]"
                    layout="total, sizes, prev, pager, next, jumper" :total="totalItems" />
            </div>
        </el-card>

        <el-dialog :show-close="false" :visible.sync="dialogCategoryOperaion" width="18%">
            <div style="padding:16px 20px;">
                <el-row>
                    <p>类别名</p>
                    <input class="dialog-input" v-model="data.name" placeholder="类别名" />
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
                <span class="channel-button" @click="cannel()">
                    取消
                </span>
                <span v-if="!isOperation" class="edit-button" @click="addOperation()">
                    确定
                </span>
                <span v-else class="edit-button" @click="updateOperation()">
                    确定
                </span>
            </span>
        </el-dialog>
    </div>
    <!-- <el-row style="background-color: #FFFFFF;padding: 5px 0;border-radius: 5px;">
        <el-row style="padding: 10px;margin-left: 5px;">
            <el-row>
                <el-input size="small" style="width: 166px;" v-model="categoryQueryDto.name" placeholder="商品类别名"
                    clearable @clear="handleFilterClear">
                    <el-button slot="append" @click="handleFilter" icon="el-icon-search"></el-button>
                </el-input>
                <span style="float: right;" class="edit-button" @click="add()">
                    新增商品类别
                </span>
            </el-row>
        </el-row>
        <el-row style="margin: 0 22px;border-top: 1px solid rgb(245,245,245);">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column prop="id" width="88" label="ID"></el-table-column>
                <el-table-column prop="name" label="商品类别名"></el-table-column>
                <el-table-column prop="isUse" width="168" label="是否启用">
                    <template slot-scope="scope">
                        <span>{{ scope.row.isUse ? '启用' : '不启用' }}</span>
                    </template>
</el-table-column>
<el-table-column label="操作" width="120">
    <template slot-scope="scope">
                        <span class="text-button" @click="handleEdit(scope.row)">编辑</span>
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
</el-table-column>
</el-table>
<el-pagination style="margin:10px 0;float: right;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
    :current-page="currentPage" :page-sizes="[10, 20]" :page-size="pageSize"
    layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
</el-row>
<el-dialog :show-close="false" :visible.sync="dialogCategoryOperaion" width="18%">
    <div style="padding:16px 20px;">
        <el-row>
            <p>*类别名</p>
            <input class="dialog-input" v-model="data.name" placeholder="商品类别名" />
        </el-row>
        <el-row>
            <p>*是否启用</p>
            <el-switch v-model="data.isUse" active-text="启用" inactive-text="不启用">
            </el-switch>
        </el-row>
    </div>
    <span slot="footer" class="dialog-footer" style="margin-top: 10px;">
        <span class="channel-button" @click="cannel()">
            取消操作
        </span>
        <span v-if="!isOperation" class="edit-button" @click="addOperation()">
            确定新增
        </span>
        <span v-else class="edit-button" @click="updateOperation()">
            确定修改
        </span>
    </span>
</el-dialog>
</el-row> -->
</template>

<script>
export default {
    data() {
        return {
            data: {},
            currentPage: 1,
            pageSize: 10,
            totalItems: 0,
            dialogCategoryOperaion: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            delectedRows: [],
            categoryQueryDto: {}, // 搜索条件
        };
    },
    created() {
        this.fetchFreshData();
    },
    methods: {
        cannel() {
            this.data = {};
            this.dialogCategoryOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除商品类别数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/category/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$notify({
                            duration: 1000,
                            title: '信息删除',
                            message: '删除成功',
                            type: 'success'
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (error) {
                    this.$message.error("商品类别信息删除异常：", error);
                    console.error(`商品类别信息删除异常：`, error);
                }
            }
        },
        /**
         * 修改商品类别信息
         */
        updateOperation() {
            // 写请求定位到【商品类别】的修改接口，这是后端提供的
            // /category/update  --- put
            this.$axios.put('/category/update', this.data).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify.success({
                        duration: 1000,
                        title: '修改操作',
                        message: data.msg
                    });
                    this.fetchFreshData();
                    this.cannel();
                }
            }).catch(error => {
                this.$notify.error({
                    title: '修改操作',
                    message: error
                });
            });
        },
        /**
         * 商品类别新增
         */
        addOperation() {
            // 写请求定位到【商品类别】的新增接口，这是后端提供的
            // /category/save  --- post
            this.$axios.post('/category/save', this.data).then(res => {
                const { data } = res;
                if (data.code === 200) {
                    this.$notify.success({
                        duration: 1000,
                        title: '新增操作',
                        message: data.msg
                    });
                    this.fetchFreshData();
                    this.cannel();
                } else {
                    this.$notify.info({
                        duration: 1000,
                        title: '新增操作',
                        message: data.msg
                    });
                }
            }).catch(error => {
                this.$notify.error({
                    title: '新增操作',
                    message: error
                });
            });
        },
        /**
         * 商品类别查询
         */
        fetchFreshData() {
            this.categoryQueryDto.current = this.currentPage;
            this.categoryQueryDto.size = this.pageSize;
            this.$axios.post('/category/query', this.categoryQueryDto).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.tableData = data.data;
                    this.totalItems = data.total;
                }
            }).catch(error => {
                this.$notify.error({
                    title: '查询操作',
                    message: error
                });
            })
        },
        add() {
            this.dialogCategoryOperaion = true;
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.handleFilter();
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
        handleEdit(row) {
            this.dialogCategoryOperaion = true;
            this.isOperation = true;
            row.userPwd = null;
            this.userAvatar = row.userAvatar;
            this.data = { ...row }
        },
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.category-page {
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

    .price-text {
        font-weight: 600;
        color: #ff5722;
    }
}
</style>