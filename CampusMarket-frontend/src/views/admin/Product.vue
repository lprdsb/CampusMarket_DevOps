<template>
    <div class="product-page">

        <!-- 筛选卡片 -->
        <el-card shadow="always" class="filter-card">
            <el-form :inline="true" size="small" label-width="auto" class="filter-form">
                <el-form-item label="类别">
                    <el-select v-model="productQueryDto.categoryId" placeholder="请选择" @change="fetchFreshData"
                        style="width: 120px">
                        <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                    <el-date-picker v-model="searchTime" type="daterange" range-separator="至" start-placeholder="开始日期"
                        end-placeholder="结束日期" @change="fetchFreshData" style="width: 250px" />
                </el-form-item>
                <el-form-item label="商品名">
                    <el-input v-model="productQueryDto.name" placeholder="输入关键词" clearable @clear="handleFilterClear"
                        style="width: 200px">
                        <el-button slot="append" icon="el-icon-search" style="background-color: #4a90e2;"
                            @click="handleFilter" />
                    </el-input>
                </el-form-item>
            </el-form>
        </el-card>

        <!-- 表格卡片 -->
        <el-card shadow="hover" class="table-card">
            <el-table :stripe="true" :data="tableData" style="width: 100%">
                <el-table-column label="头像" width="70">
                    <template slot-scope="scope">
                        <el-avatar :size="32" :src="scope.row.userAvatar" />
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="用户" width="140" />
                <el-table-column prop="name" label="商品名" />
                <el-table-column prop="categoryName" label="类别" width="110" />
                <el-table-column label="价格" width="100">
                    <template slot-scope="scope">
                        <span class="price-text">￥{{ scope.row.price }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="inventory" label="库存" width="90" />
                <el-table-column prop="createTime" label="发布时间" width="160" :sortable="true" />
                <el-table-column label="操作" width="130">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="handleDelete(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-wrapper">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-size="pageSize" :page-sizes="[10, 20]"
                    layout="total, sizes, prev, pager, next, jumper" :total="totalItems" />
            </div>
        </el-card>

        <!-- 商品详情抽屉 -->
        <el-drawer title="商品详情" :visible.sync="drawerProductOperaion" :direction="direction" size="40%">
            <div class="drawer-body">
                <h4>产品封面图</h4>
                <div class="cover-list">
                    <div class="cover-item" v-for="(cover, index) in coverList" :key="index"
                        @click="coverExpansion(cover)">
                        <img :src="cover" />
                    </div>
                </div>
                <h4 style="margin-top: 16px;">商品详情</h4>
                <div class="product-description" v-html="data.detail"></div>
            </div>
        </el-drawer>

        <!-- 放大图片对话框 -->
        <el-dialog :visible.sync="dialogCoverExpansion" width="40%">
            <div class="cover-preview">
                <img :src="cover" />
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            data: {},
            currentPage: 1,
            coverList: [],
            searchTime: [],
            cover: null, // 放大查看的产品图略
            pageSize: 10,
            totalItems: 0,
            drawerProductOperaion: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            delectedRows: [],
            dialogCoverExpansion: false,
            productQueryDto: {}, // 搜索条件
            direction: 'rtl', // 从右往左 right to left
            categoryList: [],
            bargainSelectedItem: {},
            // bargainStatus: [{ isBargain: null, name: '全部' }, { isBargain: true, name: '支持砍价' }, { isBargain: false, name: '不支持砍价' }]
        };
    },
    created() {
        this.fetchCategoryList();
        this.fetchFreshData();
        // 页面加载时，默认不启用砍价查询条件
        this.bargainSelected(this.bargainStatus[0]);
    },
    methods: {
        /**
         * 砍价状态选中事件
         * @param {*} bargain 砍价状态
         */
        bargainSelected(bargain) {
            this.bargainSelectedItem = bargain;
            this.productQueryDto.isBargain = bargain.isBargain;
            this.fetchFreshData();
        },
        /**
         * 加载商品类别数据
         */
        fetchCategoryList() {
            this.$axios.post('/category/query', {}).then(res => {
                const { data } = res; // 解构
                if (data.code === 200) {
                    this.categoryList = data.data;
                    // push 往尾部添加元素 尾插法
                    // unshift 往头部添加元素 头插法
                    this.categoryList.unshift({ id: null, name: '全部' });
                }
            }).catch(error => {
                console.log("商品类别查询异常：", error);
            })
        },
        /**
         * 产品图放大查看
         * @param {*} cover 图片链接
         */
        coverExpansion(cover) {
            this.cover = cover;
            this.dialogCoverExpansion = true;
        },
        cannel() {
            this.data = {};
            this.drawerProductOperaion = false;
            this.isOperation = false;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.delectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除商品数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.delectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/product/batchDelete`, ids);
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
                    this.$message.error("商品信息删除异常：", error);
                    console.error(`商品信息删除异常：`, error);
                }
            }
        },
        /**
         * 商品查询
         */
        async fetchFreshData() {
            let startTime = null;
            let endTime = null;
            if (this.searchTime != null && this.searchTime.length === 2) {
                const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                startTime = `${startDate.split('T')[0]}T00:00:00`;
                endTime = `${endDate.split('T')[0]}T23:59:59`;
            }
            this.productQueryDto.current = this.currentPage;
            this.productQueryDto.size = this.pageSize;
            this.productQueryDto.startTime = startTime;
            this.productQueryDto.endTime = endTime;
            this.$axios.post('/product/query', this.productQueryDto).then(res => {
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
            this.drawerProductOperaion = true;
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
        parseCoverList(coverList) {
            this.coverList = coverList.split(',');
        },
        handleEdit(row) {
            this.data = row;
            this.parseCoverList(row.coverList);
            this.drawerProductOperaion = true;
        },
        handleDelete(row) {
            this.delectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.product-page {
    padding: 10px;
}

.filter-card {
    margin-bottom: 16px;
    border-radius: 10px;

    .el-form-item {
        margin-right: 20px;
    }
}

.table-card {
    border-radius: 10px;

    .price-text {
        font-weight: 600;
        color: #ff5722;
    }
}

.pagination-wrapper {
    margin-top: 16px;
    text-align: right;
}

.drawer-body {
    padding: 10px;

    h4 {
        font-size: 15px;
        font-weight: 600;
        margin-bottom: 6px;
        color: #333;
    }

    .cover-list {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;

        .cover-item {
            border-radius: 8px;
            overflow: hidden;
            border: 1px solid #f0f0f0;
            transition: transform 0.3s ease;
            cursor: pointer;

            img {
                width: 100px;
                height: 100px;
                object-fit: cover;
                display: block;
            }

            &:hover {
                transform: scale(1.05);
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }
        }
    }

    .product-description {
        background: #f9f9f9;
        padding: 10px;
        border-radius: 6px;
        max-height: 300px;
        overflow-y: auto;
        font-size: 14px;
        line-height: 1.6;
    }
}

.cover-preview {
    display: flex;
    justify-content: center;
    align-items: center;

    img {
        width: 400px;
        height: 400px;
        object-fit: cover;
        border-radius: 10px;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);
    }
}
</style>