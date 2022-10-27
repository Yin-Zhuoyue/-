<template>
    <body class="hold-transition">
    <div id="app">
        <div class="content-header">
            <h1>任务管理</h1>
        </div>
        <div class="app-container">
            <div class="box">
                <div class="filter-container">
                    <el-input placeholder="任务名称" v-model="queryString" style="width: 200px;float:left"
                              class="filter-item"></el-input>
                    <el-button @click="getByName(queryString)" class="dalfBut" style="float:left">查询
                    </el-button>
                    <el-button type="primary" class="butT" @click="handleCreate()" style="float:left">新建</el-button>
                </div>

                <el-table size="small" current-row-key="task_id" :data="dataList" stripe highlight-current-row>
                    <el-table-column type="index" align="center" label="序号"></el-table-column>
                    <el-table-column prop="task_name" label="任务名称" align="center"></el-table-column>
                    <el-table-column prop="task_type" label="任务类型" align="center"></el-table-column>
                    <el-table-column prop="execution_time_period" label="执行周期" align="center">
                        <template slot-scope="scope">
                            <el-row type="flex" align="middle">
                                <el-col :span="10" :offset="2">
                                    <p>{{ scope.row.execution_time_period }}</p>
                                </el-col>
                                <el-col :span="8">
                                    <el-button type="primary" size="mini" @click="cronCreate(scope.row.task_id)">编辑
                                    </el-button>
                                </el-col>
                            </el-row>
                        </template>
                    </el-table-column>
                    <el-table-column prop="task_status" label="开启/关闭" align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    v-model="scope.row.task_status"
                                    :active-value="1"
                                    :inactive-value="0"
                                    @change="changeTaskStatus(scope.$index, scope.row.task_id, $event)">
                            </el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column label="执行结果管理" align="center">
                        <template slot-scope="scope">
                            <el-button type="info" size="mini" @click="getTaskResultFileList(scope.row.task_id)">详情
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="任务信息操作" align="center">
                        <template slot-scope="scope">
                            <el-tooltip placement="top" effect="light" trigger="hover" content="详情">
                                <el-button size="mini" @click="getTaskById(scope.row)">详</el-button>
                            </el-tooltip>
                            <el-tooltip placement="top" effect="light" trigger="hover" content="复制">
                                <el-button size="mini" icon="el-icon-copy-document" @click="copyTask(scope.row)"/>
                            </el-tooltip>
                            <el-tooltip placement="top" effect="light" trigger="hover" content="编辑">
                                <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(scope.row)"/>
                            </el-tooltip>
                            <el-tooltip placement="top" effect="light" trigger="hover" content="删除">
                                <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(scope.row)"/>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                </el-table>

                <el-pagination
                        @current-change="getByPage"
                        :page-size="10"
                        :pager-count="7"
                        :current-page.sync=pagination
                        layout="prev, pager, next"
                        :total="200">
                </el-pagination>

                <!-- 新建任务弹层 -->
                <div class="add-form">
                    <el-dialog title="新增任务" :visible.sync="dialogFormVisible" width="70%" :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="addTaskInfo" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="16">
                                    <el-form-item label="任务名称" prop="task.task_name">
                                        <el-input v-model="addTaskInfo.task.task_name" maxlength="70"
                                                  placeholder="最多70字" show-word-limit/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="任务类型" prop="task.task_type">
                                        <el-select v-model="addTaskInfo.task.task_type" placeholder="请选择"
                                                   style="float:left" clearable>
                                            <el-option label="邮件" value="邮件"></el-option>
                                            <el-option label="FTP" value="FTP"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求人">
                                        <el-input v-model="addTaskInfo.task.demander"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求部门">
                                        <el-input v-model="addTaskInfo.task.demand_department"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="addTaskInfo.task.task_type === '邮件'">
                                <el-col :span="24">
                                    <el-form-item label="邮件任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="收件人" prop="mailTask.recipient_mail_address">
                                        <el-input v-model="addTaskInfo.mailTask.recipient_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getMailAddress($event.target.value)"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="抄送人">
                                        <el-input v-model="addTaskInfo.mailTask.recipient_cc_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getCcMailAddress($event.target.value)"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件主题">
                                        <el-input v-model="addTaskInfo.mailTask.mail_subject" placeholder="默认与任务名称相同"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件正文">
                                        <el-input v-model="addTaskInfo.mailTask.mail_text" type="textarea" :rows="1"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="addTaskInfo.task.task_type === 'FTP'">
                                <el-col :span="24">
                                    <el-form-item label="FTP任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 地址" prop="ftpTask.ftp_host">
                                        <el-input v-model="addTaskInfo.ftpTask.ftp_host"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 端口号" prop="ftpTask.ftp_port">
                                        <el-input v-model="addTaskInfo.ftpTask.ftp_port"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 用户名" prop="ftpTask.ftp_user">
                                        <el-input v-model="addTaskInfo.ftpTask.ftp_user"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="密码" prop="ftpTask.ftp_pwd">
                                        <el-input v-model="addTaskInfo.ftpTask.ftp_pwd" type="password" show-password/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="文件存放路径">
                                        <el-input v-model="addTaskInfo.ftpTask.file_save_catalog" placeholder="默认为根目录"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-row v-show="addTaskInfo.task.task_type === 'FTP'">
                                <el-button type="primary" @click="addFtpConnectCheck()">ftp连接测试</el-button>
                                <el-button @click="dialogFormVisible = false">取消</el-button>
                                <el-button type="primary" @click="handleAdd()">提交</el-button>
                            </el-row>
                            <el-row v-show="addTaskInfo.task.task_type === '邮件'">
                                <el-button @click="dialogFormVisible = false">取消</el-button>
                                <el-button type="primary" @click="handleAdd()">提交</el-button>
                            </el-row>


                        </div>

                    </el-dialog>
                </div>

                <!-- 查看任务详情弹层 -->
                <div class="add-form">
                    <el-dialog title="查看任务" :visible.sync="getTaskInfoVisible" width="70%">
                        <el-form ref="dataAddForm" :model="taskInfo" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="16">
                                    <el-form-item label="任务名称">
                                        <el-input v-model="taskInfo.task.task_name" maxlength="70" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="任务类型">
                                        <el-select v-model="taskInfo.task.task_type" :disabled="true"
                                                   style="float:left">
                                            <el-option label="邮件" value="邮件"></el-option>
                                            <el-option label="FTP" value="FTP"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求人">
                                        <el-input v-model="taskInfo.task.demander" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求部门">
                                        <el-input v-model="taskInfo.task.demand_department" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="taskInfo.task.task_type === '邮件'">
                                <el-col :span="24">
                                    <el-form-item label="邮件任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="收件人" prop="mailTask.recipient_mail_address">
                                        <el-input v-model="taskInfo.mailTask.recipient_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getMailAddress($event.target.value)" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="抄送人">
                                        <el-input v-model="taskInfo.mailTask.recipient_cc_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getCcMailAddress($event.target.value)" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件主题">
                                        <el-input v-model="taskInfo.mailTask.mail_subject"
                                                  placeholder="默认与任务名称相同" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件正文">
                                        <el-input v-model="taskInfo.mailTask.mail_text"
                                                  type="textarea" :rows="1" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="taskInfo.task.task_type === 'FTP'">
                                <el-col :span="24">
                                    <el-form-item label="FTP任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 地址">
                                        <el-input v-model="taskInfo.ftpTask.ftp_host" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 端口号">
                                        <el-input v-model="taskInfo.ftpTask.ftp_port" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 用户名">
                                        <el-input v-model="taskInfo.ftpTask.ftp_user" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="用户名密码">
                                        <el-input v-model="taskInfo.ftpTask.ftp_pwd" type="password" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="文件存放路径">
                                        <el-input v-model="taskInfo.ftpTask.file_save_catalog"
                                                  placeholder="默认为根目录" :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-col :span="12">
                                <el-form-item label="创建时间">
                                    <el-input v-model="taskInfo.task.task_add_date" :disabled="true"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="最近编辑时间">
                                    <el-input v-model="taskInfo.task.last_edit_time" :disabled="true"/>
                                </el-form-item>
                            </el-col>
                            <el-form-item label="最近一次执行日志" label-width="20">
                                <el-input type="textarea"
                                          :rows="5"
                                          v-model="taskInfo.task.last_task_execution_process"
                                          :disabled="true"
                                />
                            </el-form-item>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-button @click="getTaskInfoVisible = false">确定</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 编辑任务弹层 -->
                <div class="add-form">
                    <el-dialog title="编辑任务" :visible.sync="editTaskInfoVisible" width="70%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="taskInfo" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="16">
                                    <el-form-item label="任务名称" prop="task.task_name">
                                        <el-input v-model="taskInfo.task.task_name" maxlength="70" placeholder="最多70字"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="任务类型" prop="task.task_type" style="float:left">
                                        <el-select v-model="taskInfo.task.task_type" placeholder="请选择" clearable>
                                            <el-option label="邮件" value="邮件"></el-option>
                                            <el-option label="FTP" value="FTP"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求人">
                                        <el-input v-model="taskInfo.task.demander"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="需求部门">
                                        <el-input v-model="taskInfo.task.demand_department"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="taskInfo.task.task_type === '邮件'">
                                <el-col :span="24">
                                    <el-form-item label="邮件任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="收件人" prop="mailTask.recipient_mail_address">
                                        <el-input v-model="taskInfo.mailTask.recipient_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getEditMailAddress($event.target.value, taskInfo.mailTask.recipient_mail_address)"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="抄送人">
                                        <el-input v-model="taskInfo.mailTask.recipient_cc_mail_address"
                                                  type="textarea" :rows="4" placeholder="可输入多个邮箱地址，分隔符不限"
                                                  @blur="getEditCcMailAddress($event.target.value)"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件主题">
                                        <el-input v-model="taskInfo.mailTask.mail_subject"
                                                  placeholder="默认与任务名称相同"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="邮件正文">
                                        <el-input v-model="taskInfo.mailTask.mail_text" type="textarea" :rows="1"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row v-show="taskInfo.task.task_type === 'FTP'">
                                <el-col :span="24">
                                    <el-form-item label="FTP任务信息"></el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 地址" prop="ftpTask.ftp_host">
                                        <el-input v-model="taskInfo.ftpTask.ftp_host"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 端口号" prop="ftpTask.ftp_port">
                                        <el-input v-model="taskInfo.ftpTask.ftp_port"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="FTP 用户名" prop="ftpTask.ftp_user">
                                        <el-input v-model="taskInfo.ftpTask.ftp_user"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="用户名密码" prop="ftpTask.ftp_pwd">
                                        <el-input v-model="taskInfo.ftpTask.ftp_pwd" type="password"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="文件存放路径">
                                        <el-input v-model="taskInfo.ftpTask.file_save_catalog" placeholder="默认为根目录"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-row v-show="taskInfo.task.task_type === 'FTP'">
                                <el-button type="primary" @click="ftpConnectCheck()">ftp连接测试</el-button>
                                <el-button @click="editTaskInfoVisible = false">取消</el-button>
                                <el-button type="primary" @click="editTask()">提交</el-button>
                            </el-row>
                            <el-row v-show="taskInfo.task.task_type === '邮件'">
                                <el-button @click="editTaskInfoVisible = false">取消</el-button>
                                <el-button type="primary" @click="editTask()">提交</el-button>
                            </el-row>
                        </div>
                    </el-dialog>
                </div>

                <!-- cron表达式生成器弹层 -->
                <div class="add-form">
                    <el-dialog title="执行周期选择" :visible.sync="cronCreateFormVisible" width="50%">
                        <el-form ref="dataAddForm" :rules="rules" label-position="right" label-width="100px">
                            <el-row>
                                <el-col :span="8">
                                    <el-select v-model="period" placeholder="请选择周期" @change="periodSelect" clearable>
                                        <el-option label="每天" value="每天"></el-option>
                                        <el-option label="每周" value="每周"></el-option>
                                        <el-option label="每月" value="每月"></el-option>
                                    </el-select>
                                </el-col>
                                <el-col :span="8">
                                    <el-select
                                            v-show="period === '每周'"
                                            v-model="week"
                                            value-key="cron"
                                            placeholder="请选择星期"
                                    >
                                        <el-option
                                                v-for="(item,index) in weekOption"
                                                :key="item.cron"
                                                :label="item.title"
                                                :value="item"
                                        ></el-option>
                                    </el-select>
                                </el-col>
                                <el-col :span="8">
                                    <el-select
                                            v-show="period === '每月'"
                                            v-model="month"
                                            value-key="cron"
                                            placeholder="请选择日期"
                                    >
                                        <el-option
                                                v-for="(item,index) in monthOption"
                                                :key="item.cron"
                                                :label="item.title"
                                                :value="item"
                                        ></el-option>
                                    </el-select>
                                </el-col>
                                <el-col :span="8">
                                    <el-select v-model="time" placeholder="请选择时间" @change="timeSelect" clearable>
                                        <el-option label="02:00" value="02:00"></el-option>
                                        <el-option label="06:30" value="06:30"></el-option>
                                        <el-option label="09:00" value="09:00"></el-option>
                                    </el-select>
                                </el-col>
                            </el-row>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="cronCreateFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="cronExpSummit()">确定</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 执行结果列表弹层 -->
                <div class="add-form">
                    <el-dialog title="执行结果文件列表" :visible.sync="resultFileListVisible" width="90%"
                               :close-on-click-modal="false">
                        <div class="filter-container">
                            <el-button type="primary" style="float:left" class="butT" @click="createFileForm()">新建文件
                            </el-button>
                        </div>
                        <el-table size="small" current-row-key="file_id" :data="resultFileList" stripe
                                  highlight-current-row>
                            <el-table-column prop="file_name" label="文件名" align="center"></el-table-column>
                            <el-table-column prop="file_type" label="文件类型" align="center"></el-table-column>
                            <el-table-column prop="data_separator" label="分隔符" align="center"></el-table-column>
                            <el-table-column prop="date_suffix_type" label="日期后缀类型" align="center"></el-table-column>
                            <el-table-column label="sql列表" align="center">
                                <template slot-scope="scope">
                                    <el-button type="info" size="mini"
                                               @click="getTaskSqlList(scope.row.file_id, scope.row.file_type)">详情
                                    </el-button>
                                </template>
                            </el-table-column>
                            <el-table-column label="文件信息操作" align="center">
                                <template slot-scope="scope">
                                    <el-button size="mini" @click="getFileById(scope.row.file_id)">查看</el-button>
                                    <el-button type="primary" size="mini" @click="updateFile(scope.row.file_id)">编辑
                                    </el-button>
                                    <el-button type="danger" size="mini" @click="deleteFile(scope.row.file_id)">删除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-dialog>
                </div>

                <!-- 新建执行结果文件弹层 -->
                <div class="add-form">
                    <el-dialog title="新增文件" :visible.sync="createFileFormVisible" width="70%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="resultFileInfo" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="文件名称" prop="file_name">
                                        <el-input v-model="resultFileInfo.file_name" maxlength="30"
                                                  placeholder="最多30字" show-word-limit/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="文件类型" prop="file_type">
                                        <el-select v-model="resultFileInfo.file_type" placeholder="请选择"
                                                   @change="getFileExtension">
                                            <el-option label="TXT" value="TXT"></el-option>
                                            <el-option label="Excel" value="Excel"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6" v-show="resultFileInfo.file_type === 'TXT'">
                                    <el-form-item label="分隔符">
                                        <el-tooltip class="item" content="默认为英文逗号" placement="top">
                                            <el-input v-model="resultFileInfo.data_separator" maxlength="3"
                                                      show-word-limit/>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="14">
                                    <el-form-item label="后缀日期类型" style="float:left">
                                        <el-select v-model="resultFileInfo.date_suffix_type"
                                                   placeholder="请选择" @change="getSuffixDatePreview">
                                            <el-option label="单日期" value="单日期"></el-option>
                                            <el-option label="日期区间" value="日期区间"></el-option>
                                            <el-option label="不加后缀日期" value="不加后缀日期"></el-option>
                                        </el-select>
                                        <el-tooltip placement="top">
                                            <div slot="content">日期后缀具体效果请见预览。预览中的日期是假设今天就是执行日当日生成的</div>
                                            <i class="el-icon-question icon-color"></i>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12" v-show="resultFileInfo.date_suffix_type === '单日期'">
                                    <el-form-item label="基准日">
                                        <el-select v-model="resultFileInfo.start_date"
                                                   placeholder="请选择" @change="getSuffixDatePreview"
                                                   :disabled="true" style="float:left">
                                            <el-option label="执行日当日" value="执行日当日"></el-option>
                                            <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                            <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="偏移量" style="float:left">
                                        <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31" :max="31"
                                                         @change="getSuffixDatePreview"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'">
                                    <div class="sub-title" align="center">起始日期</div>
                                    <el-main>
                                        <el-form-item label="基准日" style="float:left">
                                            <el-select v-model="resultFileInfo.start_date"
                                                       placeholder="请选择" @change="getSuffixDatePreview">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                                <el-option label="执行日昨日当月1日" value="执行日昨日当月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量" style="float:left">
                                            <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'" :offset="2">
                                    <div class="sub-title" align="center">结束日期</div>
                                    <el-main>
                                        <el-form-item label="基准日" style="float:left">
                                            <el-select v-model="resultFileInfo.end_date"
                                                       placeholder="请选择" @change="getSuffixDatePreview">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量" style="float:left">
                                            <el-input-number v-model="resultFileInfo.end_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-col :span="18">
                                <el-input v-model="resultFileInfo.file_name+suffixDatePreview+fileExtension"
                                          :disabled="true">
                                    <template slot="prepend">文件名预览：</template>
                                </el-input>
                            </el-col>
                            <el-button @click="createFileFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="createFile()">提交</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 查看单个执行结果文件弹层 -->
                <div class="add-form">
                    <el-dialog title="查看文件" :visible.sync="getFileFormVisible" width="70%">
                        <el-form ref="dataAddForm" :rules="rules" label-position="right" label-width="100px">
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="文件名称">
                                        <el-input v-model="resultFileInfo.file_name" maxlength="30"
                                                  placeholder="最多30字" show-word-limit :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="文件类型">
                                        <el-select v-model="resultFileInfo.file_type" placeholder="请选择"
                                                   @change="getFileExtension" :disabled="true">
                                            <el-option label="TXT" value="TXT"></el-option>
                                            <el-option label="Excel" value="Excel"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6" v-show="resultFileInfo.file_type === 'TXT'">
                                    <el-form-item label="分隔符">
                                        <el-input v-model="resultFileInfo.data_separator" maxlength="3"
                                                  :disabled="true"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="14">
                                    <el-form-item label="后缀日期类型" style="float:left">
                                        <el-select v-model="resultFileInfo.date_suffix_type"
                                                   placeholder="请选择" @change="getSuffixDatePreview" :disabled="true">
                                            <el-option label="单日期" value="单日期"></el-option>
                                            <el-option label="日期区间" value="日期区间"></el-option>
                                            <el-option label="不加后缀日期" value="不加后缀日期"></el-option>
                                        </el-select>
                                        <el-tooltip placement="top">
                                            <div slot="content">日期后缀具体效果请见预览。预览中的日期是假设今天就是执行日当日生成的</div>
                                            <i class="el-icon-question icon-color"></i>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12" v-show="resultFileInfo.date_suffix_type === '单日期'">
                                    <el-form-item label="基准日">
                                        <el-select v-model="resultFileInfo.start_date"  style="float:left"
                                                   placeholder="请选择" @change="getSuffixDatePreview" :disabled="true">
                                            <el-option label="执行日当日" value="执行日当日"></el-option>
                                            <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                            <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="偏移量"  style="float:left">
                                        <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31" :max="31"
                                                         @change="getSuffixDatePreview"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'">
                                    <div class="sub-title" align="center">起始日期</div>
                                    <el-main>
                                        <el-form-item label="基准日">
                                            <el-select v-model="resultFileInfo.start_date"  style="float:left"
                                                       placeholder="请选择" @change="getSuffixDatePreview"
                                                       :disabled="true">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量"  style="float:left">
                                            <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview" :disabled="true"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'" :offset="2">
                                    <div class="sub-title" align="center">结束日期</div>
                                    <el-main>
                                        <el-form-item label="基准日">
                                            <el-select v-model="resultFileInfo.end_date"  style="float:left"
                                                       placeholder="请选择" @change="getSuffixDatePreview"
                                                       :disabled="true">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量"  style="float:left">
                                            <el-input-number v-model="resultFileInfo.end_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview" :disabled="true"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-col :span="18">
                                <el-input v-model="resultFileInfo.file_name+suffixDatePreview+fileExtension"
                                          :disabled="true">
                                    <template slot="prepend">文件名预览：</template>
                                </el-input>
                            </el-col>
                            <el-button @click="getFileFormVisible = false">确定</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 编辑执行结果文件弹层 -->
                <div class="add-form">
                    <el-dialog title="编辑文件" :visible.sync="editFileFormVisible" width="70%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="resultFileInfo" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row :gutter="20">
                                <el-col :span="12">
                                    <el-form-item label="文件名称" prop="file_name">
                                        <el-input v-model="resultFileInfo.file_name" maxlength="30"
                                                  placeholder="最多30字" show-word-limit/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="文件类型" prop="file_type">
                                        <el-select v-model="resultFileInfo.file_type" @change="getFileExtension"
                                                   :disabled="true">
                                            <el-option label="TXT" value="TXT"></el-option>
                                            <el-option label="Excel" value="Excel"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6" v-show="resultFileInfo.file_type === 'TXT'">
                                    <el-form-item label="分隔符" prop="data_separator">
                                        <el-input v-model="resultFileInfo.data_separator" maxlength="3"
                                                  show-word-limit/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="14">
                                    <el-form-item label="后缀日期类型"  style="float:left">
                                        <el-select v-model="resultFileInfo.date_suffix_type"
                                                   placeholder="请选择" @change="getSuffixDatePreview">
                                            <el-option label="单日期" value="单日期"></el-option>
                                            <el-option label="日期区间" value="日期区间"></el-option>
                                            <el-option label="不加后缀日期" value="不加后缀日期"></el-option>
                                        </el-select>
                                        <el-tooltip placement="top">
                                            <div slot="content">日期后缀具体效果请见预览。预览中的日期是假设今天就是执行日当日生成的</div>
                                            <i class="el-icon-question icon-color"></i>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12" v-show="resultFileInfo.date_suffix_type === '单日期'">
                                    <el-form-item label="基准日">
                                        <el-select v-model="resultFileInfo.start_date"  style="float:left"
                                                   placeholder="请选择" @change="getSuffixDatePreview" :disabled="true">
                                            <el-option label="执行日当日" value="执行日当日"></el-option>
                                            <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                            <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="偏移量"  style="float:left">
                                        <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31" :max="31"
                                                         @change="getSuffixDatePreview"/>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'">
                                    <div class="sub-title" align="center">起始日期</div>
                                    <el-main>
                                        <el-form-item label="基准日" style="float:left">
                                            <el-select v-model="resultFileInfo.start_date"
                                                       placeholder="请选择" @change="getSuffixDatePreview">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                                <el-option label="执行日昨日当月1日" value="执行日昨日当月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量" style="float:left">
                                            <el-input-number v-model="resultFileInfo.start_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                                <el-col :span="11" v-show="resultFileInfo.date_suffix_type === '日期区间'" :offset="2">
                                    <div class="sub-title" align="center">结束日期</div>
                                    <el-main>
                                        <el-form-item label="基准日" style="float:left">
                                            <el-select v-model="resultFileInfo.end_date"
                                                       placeholder="请选择" @change="getSuffixDatePreview">
                                                <el-option label="执行日当日" value="执行日当日"></el-option>
                                                <el-option label="执行日当月1日" value="执行日当月1日"></el-option>
                                                <el-option label="执行日上月1日" value="执行日上月1日"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="偏移量" style="float:left">
                                            <el-input-number v-model="resultFileInfo.end_date_offset" :min="-31"
                                                             :max="31"
                                                             @change="getSuffixDatePreview"/>
                                        </el-form-item>
                                    </el-main>
                                </el-col>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-col :span="12">
                                <el-input v-model="resultFileInfo.file_name+suffixDatePreview+fileExtension"
                                          :disabled="true">
                                    <template slot="prepend">文件名预览：</template>
                                </el-input>
                            </el-col>
                            <el-button @click="editFileFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="editFile()">提交</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- sql列表弹层 -->
                <div class="add-form">
                    <el-dialog title="sql列表" :visible.sync="sqlListVisible" width="60%" :close-on-click-modal="false">
                        <div class="filter-container">
                            <el-button type="primary" class="butT" @click="createSqlForm()" style="float:left">新建sql
                            </el-button>
                        </div>
                        <el-table size="small" current-row-key="sql_id" :data="sqlList" stripe highlight-current-row>
                            <el-table-column prop="sql_name" label="sql名称" align="center"></el-table-column>
                            <el-table-column prop="database_name" label="数据库名称" align="center"></el-table-column>
                            <el-table-column label="sql信息操作" align="center">
                                <template slot-scope="scope">
                                    <el-button size="mini" @click="getSqlById(scope.row.sql_id)">查看</el-button>
                                    <el-button type="primary" size="mini" @click="updateSql(scope.row.sql_id)">编辑
                                    </el-button>
                                    <el-button type="danger" size="mini" @click="deleteSql(scope.row.sql_id)">删除
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-dialog>
                </div>

                <!-- 新建sql弹层 -->
                <div class="add-form">
                    <el-dialog title="新增sql" :visible.sync="createSqlFormVisible" width="80%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="addTaskSql" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="6">
                                    <el-form-item label="数据库名称" prop="database_name" style="float:left">
                                        <el-select v-model="addTaskSql.database_name" placeholder="请选择">
                                            <el-option label="综调" value="综调"></el-option>
                                            <el-option label="综告" value="综告"></el-option>
                                            <el-option label="统一网元库" value="统一网元库"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="sql名称">
                                        <el-tooltip class="item" content="sql名就是生成的excel中的sheet名，可不填" placement="top">
                                            <el-input v-model="addTaskSql.sql_name" maxlength="15"
                                                      show-word-limit/>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="10" :offset="2">
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate)">
                                        <el-button slot="reference">时间参数：今日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate,'mm')">
                                        <el-button slot="reference">时间参数：当月1日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover"
                                                content="trunc(trunc(sysdate,'mm') - 1,'mm')">
                                        <el-button slot="reference">时间参数：上月1日</el-button>
                                    </el-popover>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-form-item label="sql脚本" prop="sql_text">
                                    <el-input v-model="addTaskSql.sql_text" type="textarea" :rows="18"/>
                                </el-form-item>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-col :span="4" :offset="14">
                                <el-button type="primary" @click="addSqlCheck()">sql运行测试</el-button>
                                <el-tooltip placement="top">
                                    <div slot="content">sql运行测试不会生成数据，
                                        只是测试sql脚本是否能通过编译以及用到的表是否有权限访问
                                    </div>
                                    <i class="el-icon-question icon-color"></i>
                                </el-tooltip>
                            </el-col>
                            <el-button @click="createSqlFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="createSql()">提交</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 编辑sql弹层 -->
                <div class="add-form">
                    <el-dialog title="编辑sql" :visible.sync="editSqlFormVisible" width="80%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="taskSql" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="6">
                                    <el-form-item label="数据库名称" prop="database_name" style="float:left">
                                        <el-select v-model="taskSql.database_name" placeholder="请选择">
                                            <el-option label="综调" value="综调"></el-option>
                                            <el-option label="综告" value="综告"></el-option>
                                            <el-option label="统一网元库" value="统一网元库"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="sql名称">
                                        <el-tooltip class="item" content="sql名就是生成的excel中的sheet名，可不填" placement="top">
                                            <el-input v-model="taskSql.sql_name" maxlength="15"
                                                      show-word-limit/>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="10" :offset="2">
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate)">
                                        <el-button slot="reference">时间参数：今日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate,'mm')">
                                        <el-button slot="reference">时间参数：当月1日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover"
                                                content="trunc(trunc(sysdate,'mm') - 1,'mm')">
                                        <el-button slot="reference">时间参数：上月1日</el-button>
                                    </el-popover>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-form-item label="sql脚本" prop="sql_text">
                                    <el-input v-model="taskSql.sql_text" type="textarea" :rows="18"/>
                                </el-form-item>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <el-col :span="4" :offset="14">
                                <el-button type="primary" @click="sqlCheck()">sql运行测试</el-button>
                                <el-tooltip placement="top">
                                    <div slot="content">sql运行测试不会生成数据，
                                        只是测试sql脚本是否能通过编译以及用到的表是否有权限访问
                                    </div>
                                    <i class="el-icon-question icon-color"></i>
                                </el-tooltip>
                            </el-col>
                            <el-button @click="editSqlFormVisible = false">取消</el-button>
                            <el-button type="primary" @click="editSql()">提交</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!-- 查看sql弹层 -->
                <div class="add-form">
                    <el-dialog title="查看sql" :visible.sync="getSqlFormVisible" width="80%"
                               :close-on-click-modal="false">
                        <el-form ref="dataAddForm" :model="taskSql" :rules="rules" label-position="right"
                                 label-width="100px">
                            <el-row>
                                <el-col :span="6">
                                    <el-form-item label="数据库名称" prop="database_name" style="float:left">
                                        <el-select v-model="taskSql.database_name" placeholder="请选择" :disabled="true">
                                            <el-option label="综调" value="综调"></el-option>
                                            <el-option label="综告" value="综告"></el-option>
                                            <el-option label="统一网元库" value="统一网元库"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="6">
                                    <el-form-item label="sql名称">
                                        <el-tooltip class="item" content="sql名就是生成的excel中的sheet名，可不填"
                                                    placement="top" :disabled="true">
                                            <el-input v-model="taskSql.sql_name" maxlength="10" :disabled="true"
                                                      show-word-limit/>
                                        </el-tooltip>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="10" :offset="2">
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate)">
                                        <el-button slot="reference">时间参数：今日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover" content="trunc(sysdate,'mm')">
                                        <el-button slot="reference">时间参数：当月1日</el-button>
                                    </el-popover>
                                    <el-popover placement="top-start" trigger="hover"
                                                content="trunc(trunc(sysdate,'mm') - 1,'mm')">
                                        <el-button slot="reference">时间参数：上月1日</el-button>
                                    </el-popover>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-form-item label="sql脚本" prop="sql_text">
                                    <el-input v-model="taskSql.sql_text" type="textarea" :rows="18" :disabled="true"/>
                                </el-form-item>
                            </el-row>
                        </el-form>

                        <div slot="footer" class="dialog-footer">
                            <!--              <el-col :span="4" :offset="14">-->
                            <!--                <el-button type="primary" @click="sqlCheck()">sql运行测试</el-button>-->
                            <!--                <el-tooltip placement="top">-->
                            <!--                  <div slot="content">sql运行测试不会生成数据，-->
                            <!--                    只是测试sql脚本是否能通过编译以及用到的表是否有权限访问-->
                            <!--                  </div>-->
                            <!--                  <i class="el-icon-question icon-color"></i>-->
                            <!--                </el-tooltip>-->
                            <!--              </el-col>-->
                            <el-button @click="getSqlFormVisible = false">确定</el-button>
                        </div>
                    </el-dialog>
                </div>
            </div>
        </div>
    </div>
    </body>

</template>

<script>
    export default {
        name: "TaskView",
        data() {
            return {
                user_name: window.sessionStorage.getItem('user_name'),
                pagination: 1,
                queryString: "",
                dataList: [],                   // 主页面要展示的列表数据
                resultFileList: [],              // 执行结果文件列表
                sqlList: [],                     // sql列表
                taskId: 0,                       // 编辑执行周期时需要的task_id（在data中有不同位置的重复定义，需优化）
                addTaskInfo: {
                    "task": {},
                    "mailTask": {},
                    "ftpTask": {}
                },              // 新增任务信息的数据模型
                taskInfo: {
                    "task": {},
                    "mailTask": {},
                    "ftpTask": {}
                },                 // 查看、编辑已有任务的数据模型
                statusInfo: {
                    "task_id": 0,
                    "task_status": 0
                },               // 任务的使用状态，默认为关闭
                periodInfo: {
                    "task_id": 0,
                    "execution_time_period": "",
                    "cron_expression": "",
                },      // 任务的周期信息，与数据库中的字段对应
                period: "",                      // 用户选择周期时的第一个参数，前端变量，不会直接传到后端，下面几个也是这样。
                week: null,
                month: null,
                time: "",
                weekOption: [
                    {
                        title: '星期一',
                        value: '一',
                        cron: 2,
                    },
                    {
                        title: '星期二',
                        value: '二',
                        cron: 3,
                    },
                    {
                        title: '星期三',
                        value: '三',
                        cron: 4,
                    },
                    {
                        title: '星期四',
                        value: '四',
                        cron: 5,
                    },
                    {
                        title: '星期五',
                        value: '五',
                        cron: 6,
                    },
                    {
                        title: '星期六',
                        value: '六',
                        cron: 7,
                    },
                    {
                        title: '星期日',
                        value: '日',
                        cron: 1,
                    },
                ],
                monthOption: [],
                resultFileInfo: {},              // 执行结果文件的数据模型
                suffixDatePreview: "",
                fileExtension: "",
                addTaskSql: {},                  // 新增sql脚本的数据模型，之所以与下面的区分开，是因为要尽可能保存新增时的编辑状态
                taskSql: {},                     // sql脚本的数据模型

                dialogFormVisible: false,       // 新建任务表单是否可见
                editTaskInfoVisible: false,      // 编辑任务表单是否可见
                cronCreateFormVisible: false,    // 编辑cron表达式窗口是否可见
                getTaskInfoVisible: false,       // 查看任务详细信息窗口是否可见
                resultFileListVisible: false,    // 执行结果文件列表（窗口形式）是否可见
                createFileFormVisible: false,    // 新建执行结果文件窗口是否可见
                getFileFormVisible: false,       // 查看执行结果文件窗口是否可见
                editFileFormVisible: false,      // 编辑执行结果文件窗口是否可见
                sqlListVisible: false,           // sql列表窗口是否可见
                createSqlFormVisible: false,     // 创建sql窗口是否可见
                editSqlFormVisible: false,       // 编辑sql窗口是否可见
                getSqlFormVisible: false,        // 查看sql窗口是否可见
                menuVisible: false,

                rules: {//校验规则
                    task: {
                        task_name: [{required: true, message: '任务名称为必填项', trigger: 'blur'}],
                        task_type: [{required: true, message: '任务类别为必填项', trigger: 'blur'}],
                    },
                    mailTask: {recipient_mail_address: [{required: true, message: '收件人为必填项', trigger: 'blur'}]},
                    ftpTask: {
                        ftp_host: [{required: true, message: 'ftp host为必填项', trigger: 'blur'}],
                        ftp_port: [{required: true, message: 'ftp 端口为必填项', trigger: 'blur'}],
                        ftp_user: [{required: true, message: '用户名为必填项', trigger: 'blur'}],
                        ftp_pwd: [{required: true, message: '密码为必填项', trigger: 'blur'}],
                    },
                    file_name: [{required: true, message: '文件名称为必填项', trigger: 'blur'}],
                    file_type: [{required: true, message: '文件类别为必填项', trigger: 'blur'}],
                    database_name: [{required: true, message: '数据库名称为必填项', trigger: 'blur'}],
                    sql_text: [{required: true, message: 'sql脚本为必填项', trigger: 'blur'}],
                },
            }
        },
        created() {
            this.getAll();
            this.initCronData();
        },

        methods: {

            // 任务列表
            getAll() {
                const param = {"userName": this.user_name, "pagination": this.pagination, "rowNum": 10};
                this.$axios.post("/taskInfo/user", param).then((res) => {
                    this.dataList = res.data.data;
                });
            },

            // 根据任务名查询
            getByName(taskName) {
                taskName = taskName.trim();
                if (typeof taskName === "undefined" || taskName === null || taskName === "") {
                    this.getAll();
                } else {
                    const param = {
                        "taskName": taskName,
                        "userName": this.user_name,
                        "pagination": this.pagination,
                        "rowNum": 10
                    };
                    this.$axios.post("/taskInfo/taskName", param).then((res) => {
                        this.dataList = res.data.data;
                    });
                }
            },

            getByPage() {
                if (typeof this.queryString === "undefined" || this.queryString === null || this.queryString === "") {
                    this.getAll();
                } else
                    this.getByName(this.queryString);
            },

            // 弹出添加窗口
            handleCreate() {
                this.dialogFormVisible = true;
            },

            // 任务复制功能
            copyTask(row){
                this.$confirm("确认要复制当前任务吗？", "提示", {
                    type: 'info'
                }).then(() => {
                    this.tempData = {};
                    this.tempData.taskId = row.task_id;
                    this.tempData.user = this.user_name;
                    this.$axios.post("/taskInfo/copyTask", this.tempData).then((res) => {
                        if (res.data.code === 20011) {
                            this.$message.success("复制成功");
                        } else if (res.data.code === 20010) {
                            this.$message.error("复制失败");
                        } else {
                            this.$message.error(res.data.msg);
                        }
                    }).finally(() => {
                        this.getAll();
                    });
                }).catch(() => {
                this.$message.info("取消复制操作");
            });
            },

            // 添加新任务
            handleAdd() {
                if (this.addTaskInfo.task.task_type === "邮件") {
                    if (!this.addTaskInfo.task.task_name || !this.addTaskInfo.mailTask.recipient_mail_address) {
                        this.$message({message: '请完成必填项的输入！', type: 'warning'});
                        return
                    }
                } else if (this.addTaskInfo.task.task_type === "FTP") {
                    if (!this.addTaskInfo.task.task_name || !this.addTaskInfo.ftpTask.ftp_host || !this.addTaskInfo.ftpTask.ftp_port ||
                        !this.addTaskInfo.ftpTask.ftp_pwd || !this.addTaskInfo.ftpTask.ftp_user) {
                        this.$message({message: '请完成必填项的输入！', type: 'warning'});
                        return
                    }
                }
                this.addTaskInfo.task.task_creator = this.user_name;
                this.$axios.post("/taskInfo", this.addTaskInfo).then((res) => {
                    //如果操作成功，关闭弹层，显示数据
                    if (res.data.code === 20011) {
                        this.dialogFormVisible = false;
                        this.$message.success("添加成功");
                    } else if (res.data.code === 20010) {
                        this.$message.error("添加失败");
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.addTaskInfo = {"task": {}, "mailTask": {}, "ftpTask": {}}; // 重置addTaskInfo
                    this.getAll();
                });
            },

            // 查看任务信息
            getTaskById(row) {
                this.$axios.get("/taskInfo/" + row.task_id).then((res) => {
                    if (res.data.code === 20041) {
                        this.taskInfo = res.data.data;

                        if (this.taskInfo.ftpTask === null) {
                            this.taskInfo.ftpTask = {};     // 避免NoneType异常，因为后端传来的数据ftpTask可能为null
                        } else if (this.taskInfo.mailTask === null) {
                            this.taskInfo.mailTask = {};    // 同上
                        }
                        this.getTaskInfoVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },

            // 弹出编辑窗口
            handleUpdate(row) {
                this.$axios.get("/taskInfo/" + row.task_id).then((res) => {
                    if (res.data.code === 20041) {
                        this.taskInfo = res.data.data;
                        if (this.taskInfo.ftpTask === null) {
                            this.taskInfo.ftpTask = {};
                        } else if (this.taskInfo.mailTask === null) {
                            this.taskInfo.mailTask = {};
                        }
                        this.editTaskInfoVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            //编辑
            editTask() {
                if (this.taskInfo.task.task_type === "邮件") {
                    if (!this.taskInfo.task.task_name || !this.taskInfo.mailTask.recipient_mail_address) {
                        this.$message({message: '请完成必填项的输入！', type: 'warning'});
                        return
                    }
                } else if (this.taskInfo.task.task_type === "FTP") {
                    if (!this.taskInfo.task.task_name || !this.taskInfo.ftpTask.ftp_host || !this.taskInfo.ftpTask.ftp_port ||
                        !this.taskInfo.ftpTask.ftp_pwd || !this.taskInfo.ftpTask.ftp_user) {
                        this.$message({message: '请完成必填项的输入！', type: 'warning'});
                        return
                    }
                }

                this.$axios.put("/taskInfo", this.taskInfo).then((res) => {
                    //如果操作成功，关闭弹层，显示数据
                    if (res.data.code === 20031) {
                        this.editTaskInfoVisible = false;
                        this.$message.success("修改成功");
                    } else if (res.data.code === 20030) {
                        this.$message.error("修改失败");
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.getAll();
                });
            },

            // 打开任务执行结果文件列表
            getTaskResultFileList(taskId) {
                this.$axios.get("/taskResultFile/files/" + taskId).then((res) => {
                    if (res.data.code === 20041) {
                        this.resultFileList = res.data.data;
                        this.resultFileListVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.task_id = taskId;  // 记录当前任务的任务id，方便文件增删改查时定位文件所属任务
                });
            },

            // 打开新增文件弹窗
            createFileForm() {
                this.resultFileInfo = {
                    "file_name": "",
                    "date_suffix_type": "单日期",
                    "start_date_offset": 0,
                    "start_date": "执行日当日",
                    "end_date_offset": 0,
                    "end_date": "执行日当日",
                    "data_separator": ","
                };
                this.createFileFormVisible = true;
                this.getSuffixDatePreview();
            },
            // 新建执行结果文件
            createFile() {
                if (!this.resultFileInfo.file_name || !this.resultFileInfo.file_type) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.resultFileInfo.task_id = this.task_id;
                if (this.resultFileInfo.file_type === "Excel")
                    this.resultFileInfo.data_separator = "";
                this.$axios.post("/taskResultFile", this.resultFileInfo).then((res) => {
                    //如果操作成功，关闭弹层，显示数据
                    if (res.data.code === 20011) {
                        this.createFileFormVisible = false;
                        this.$message.success("添加成功");
                    } else if (res.data.code === 20010) {
                        this.$message.error("添加失败");
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.getTaskResultFileList(this.task_id);
                });
            },

            // 查看单个文件
            getFileById(fileId) {
                this.$axios.get("/taskResultFile/" + fileId).then((res) => {
                    if (res.data.code === 20041) {
                        this.resultFileInfo = res.data.data;
                        this.getSuffixDatePreview();
                        if (this.resultFileInfo.file_type === "Excel")
                            this.fileExtension = ".xlsx";
                        else if (this.resultFileInfo.file_type === "TXT")
                            this.fileExtension = ".txt";
                        this.getFileFormVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },

            // 弹出编辑文件窗口
            updateFile(fileId) {
                this.$axios.get("/taskResultFile/" + fileId).then((res) => {
                    if (res.data.code === 20041) {
                        this.resultFileInfo = res.data.data;
                        this.getSuffixDatePreview();
                        this.editFileFormVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 提交文件编辑结果
            editFile() {
                if (this.resultFileInfo.file_type === "Excel") {
                    this.resultFileInfo.data_separator = "";
                    if (!this.resultFileInfo.file_name) {
                        this.$message({message: '请完成必填项的输入！', type: 'warning'});
                        return
                    }
                } else if (!this.resultFileInfo.file_name || !this.resultFileInfo.file_type || !this.resultFileInfo.data_separator) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.$axios.put("/taskResultFile", this.resultFileInfo).then((res) => {
                    //如果操作成功，关闭弹层，显示数据
                    if (res.data.code === 20031) {
                        this.editFileFormVisible = false;
                        this.$message.success("修改成功");
                    } else if (res.data.code === 20030) {
                        this.$message.error("修改失败");
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.getTaskResultFileList(this.task_id);
                });
            },

            // 删除结果文件
            deleteFile(fileId) {
                this.$confirm("此操作永久删除当前数据，是否继续？", "提示", {type: 'info'}).then(() => {
                    this.$axios.delete("/taskResultFile/" + fileId).then((res) => {
                        if (res.data.code === 20021) {
                            this.$message.success("删除成功");
                        } else {
                            this.$message.error("删除失败");
                        }
                    }).finally(() => {
                        this.getTaskResultFileList(this.task_id);
                    });
                }).catch(() => {
                    //3.取消删除
                    this.$message.info("取消删除操作");
                });
            },

            // 文件日期后缀预览
            getSuffixDatePreview() {
                if (this.resultFileInfo.date_suffix_type === "单日期") {
                    const date = new Date();
                    date.setDate(date.getDate() + this.resultFileInfo.start_date_offset);
                    const year = date.getFullYear();
                    const month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                    const day = date.getDate() + 1 < 10 ? "0" + date.getDate() : date.getDate();
                    this.suffixDatePreview = year + "." + month + "." + day;
                } else if (this.resultFileInfo.date_suffix_type === "日期区间") {
                    const startDate = new Date();
                    if (this.resultFileInfo.start_date === "执行日当日") {
                        startDate.setDate(startDate.getDate() + this.resultFileInfo.start_date_offset);
                    } else if (this.resultFileInfo.start_date === "执行日当月1日") {
                        startDate.setDate(1 + this.resultFileInfo.start_date_offset);
                    } else if (this.resultFileInfo.start_date === "执行日上月1日") {
                        startDate.setMonth(startDate.getMonth() - 1);
                        startDate.setDate(1 + this.resultFileInfo.start_date_offset);
                    } else if (this.resultFileInfo.start_date === "执行日昨日当月1日") {
                        startDate.setDate(startDate.getDate() - 1);     // 不要把上下两行并到一起
                        startDate.setDate(1 + this.resultFileInfo.start_date_offset);
                    }
                    const year1 = startDate.getFullYear();
                    const month1 = startDate.getMonth() + 1 < 10 ? "0" + (startDate.getMonth() + 1) : startDate.getMonth() + 1;
                    const day1 = startDate.getDate() + 1 < 10 ? "0" + startDate.getDate() : startDate.getDate();
                    const start = year1 + "." + month1 + "." + day1;
                    const endDate = new Date();
                    if (this.resultFileInfo.end_date === "执行日当日") {
                        endDate.setDate(endDate.getDate() + this.resultFileInfo.end_date_offset);
                    } else if (this.resultFileInfo.end_date === "执行日当月1日") {
                        endDate.setDate(1 + this.resultFileInfo.end_date_offset);
                    } else if (this.resultFileInfo.end_date === "执行日上月1日") {
                        endDate.setMonth(endDate.getMonth() - 1);
                        endDate.setDate(1 + this.resultFileInfo.end_date_offset);
                    }
                    const year2 = endDate.getFullYear();
                    const month2 = endDate.getMonth() + 1 < 10 ? "0" + (endDate.getMonth() + 1) : endDate.getMonth() + 1;
                    const day2 = endDate.getDate() + 1 < 10 ? "0" + endDate.getDate() : endDate.getDate();
                    const end = year2 + "." + month2 + "." + day2;
                    this.suffixDatePreview = start + "-" + end;
                } else {
                    this.suffixDatePreview = "";
                }
            },

            // 文件扩展名预览
            getFileExtension() {
                if (this.resultFileInfo.file_type === "TXT") {
                    this.fileExtension = ".txt"
                } else if (this.resultFileInfo.file_type === "Excel") {
                    this.fileExtension = ".xlsx"
                }
            },

            // 打开文件的sql列表
            getTaskSqlList(fileId, fileType) {
                this.$axios.get("/taskSql/sqls/" + fileId).then((res) => {
                    if (res.data.code === 20041) {
                        this.sqlList = res.data.data;
                        this.sqlListVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.file_id = fileId;      // 记录当前文件的id，方便sql增删改查时定位sql所属文件
                    this.file_type = fileType;  // 记录当前文件的类型，方便检查文件的sql脚本个数，txt文件只能有一个脚本，excel最多10个
                });
            },

            // 打开新增sql窗口
            createSqlForm() {
                this.$axios.get("/taskCheck/" + this.file_id).then((res) => {
                    if (res.data.code === 20041) {
                        this.sqlCount = res.data.data;
                        if (this.file_type === "TXT" && this.sqlCount === 0)
                            this.createSqlFormVisible = true;
                        else if (this.file_type === "TXT" && this.sqlCount > 0) {
                            this.$message({message: 'TXT文件只能有一个sql脚本！', type: 'warning'});
                        } else if (this.file_type === "Excel" && this.sqlCount >= 0 && this.sqlCount <= 10)
                            this.createSqlFormVisible = true;
                        else if (this.file_type === "Excel" && this.sqlCount > 10) {
                            this.$message({message: 'Excel文件最多只能有10个sql脚本！', type: 'warning'});
                        }
                    } else
                        this.$message({message: '文件脚本数检查异常！', type: 'warning'});
                });
            },
            // 新增sql脚本
            createSql() {
                if (!this.addTaskSql.database_name || !this.addTaskSql.sql_text) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.$axios.post("/taskCheck/sqlCheck", this.addTaskSql).then((res) => {// 脚本必须通过编译才能提交
                    if (res.data.data) {// 如果返回的结果数据不为null，则表示脚本运行有误，数据记录的是脚本的报错信息
                        this.$message.error("脚本没有通过编译，请测试无误后再提交");
                    }
                    else {
                        this.addTaskSql.file_id = this.file_id;
                        this.addTaskSql.task_id = this.task_id;
                        this.$axios.post("/taskSql", this.addTaskSql).then((res) => {
                            //如果操作成功，关闭弹层，显示数据
                            if (res.data.code === 20011) {
                                this.createSqlFormVisible = false;
                                this.$message.success("添加成功");
                            } else if (res.data.code === 20010) {
                                this.$message.error("添加失败");
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        }).finally(() => {
                            this.getTaskSqlList(this.file_id, this.file_type);
                            this.addTaskSql = {};
                        });
                    }
                }).finally(() => {});
            },

            // 打开sql编辑窗口
            updateSql(sqlId) {
                this.$axios.get("/taskSql/" + sqlId).then((res) => {
                    if (res.data.code === 20041) {
                        this.taskSql = res.data.data;
                        this.editSqlFormVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            // 提交sql编辑结果
            editSql() {
                if (!this.taskSql.database_name || !this.taskSql.sql_text) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.$axios.post("/taskCheck/sqlCheck", this.taskSql).then((res) => {// 脚本必须通过编译才能提交
                    if (res.data.data) {// 如果返回的结果数据不为null，则表示脚本运行有误，数据记录的是脚本的报错信息
                        this.$message.error("脚本没有通过编译，请测试无误后再提交");
                    }
                    else {
                        this.taskSql.file_id = this.file_id;
                        this.$axios.put("/taskSql", this.taskSql).then((res) => {
                            //如果操作成功，关闭弹层，显示数据
                            if (res.data.code === 20031) {
                                this.editSqlFormVisible = false;
                                this.$message.success("修改成功");
                            } else if (res.data.code === 20030) {
                                this.$message.error("修改失败");
                            } else {
                                this.$message.error(res.data.msg);
                            }
                        }).finally(() => {
                            this.getTaskSqlList(this.file_id, this.file_type);
                        });
                    }
                }).finally(() => {});


            },

            // 查看sql
            getSqlById(sqlId) {
                this.$axios.get("/taskSql/" + sqlId).then((res) => {
                    if (res.data.code === 20041) {
                        this.taskSql = res.data.data;
                        this.getSqlFormVisible = true;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
            },

            // 删除sql脚本
            deleteSql(sqlId) {
                this.$confirm("此操作永久删除当前数据，是否继续？", "提示", {type: 'info'}).then(() => {
                    this.$axios.delete("/taskSql/" + sqlId).then((res) => {
                        if (res.data.code === 20021) {
                            this.$message.success("删除成功");
                        } else {
                            this.$message.error("删除失败");
                        }
                    }).finally(() => {
                        this.getTaskSqlList(this.file_id, this.file_type);
                    });
                }).catch(() => {
                    //3.取消删除
                    this.$message.info("取消删除操作");
                });
            },

            // 删除任务
            handleDelete(row) {
                //1.弹出提示框
                this.$confirm("此操作永久删除当前数据，是否继续？", "提示", {
                    type: 'info'
                }).then(() => {
                    //2.做删除业务
                    this.$axios.delete("/taskInfo/" + row.task_id).then((res) => {
                        if (res.data.code === 20021) {
                            this.$message.success("删除成功");
                        } else {
                            this.$message.error("删除失败");
                        }
                    }).finally(() => {
                        this.getAll();
                    });
                }).catch(() => {
                    //3.取消删除
                    this.$message.info("取消删除操作");
                });
            },

            // cron表达式弹窗
            cronCreate(taskId) {
                // 清空表单
                this.time = "";
                this.week = null;
                this.month = null;
                this.period = "";
                this.taskId = taskId; // 获得任务task_id，用于编辑执行周期时定位该任务
                this.cronCreateFormVisible = true;
            },

            // cron参数初始化
            initCronData() {
                let arr = [];
                for (let i = 1; i < 32; i++) {
                    arr.push({
                        title: i + '日',
                        value: i + '日',
                        cron: i,
                    })
                }
                this.monthOption = arr;
            },
            periodSelect(t) {
                this.period = t;
                if (t === '每周' && !this.week) {
                    this.week = this.weekOption[0]
                }
                if (t === '每月' && !this.month) {
                    this.month = this.monthOption[0]
                }
            },
            timeSelect(t) {
                this.time = t;
            },
            // cron表达式提交
            cronExpSummit() {
                if (!this.time) {
                    this.$message({
                        message: '请选择时间!',
                        type: 'warning',
                    });
                    return
                }
                let dataArr = [];
                let timeCron;
                let clockCornArr = this.time.split(':').reverse();
                if (this.period === '每天') {
                    dataArr.push(this.period, this.time);
                    timeCron = "00 " + clockCornArr.join(' ') + ' * * ?'
                } else if (this.period === '每月') {
                    dataArr.push(this.period + this.month.title, this.time);
                    timeCron = "00 " + clockCornArr.join(' ') + ' ' + this.month.cron + ' * ?'
                } else if (this.period === '每周') {
                    // 每周
                    dataArr.push(this.period + this.week.value, this.time);
                    timeCron = "00 " + clockCornArr.join(' ') + ' ? * ' + this.week.cron
                }
                this.periodInfo.task_id = this.taskId;
                this.periodInfo.execution_time_period = dataArr.join(" ");
                this.periodInfo.cron_expression = timeCron;
                this.$axios.put("/taskInfo/taskPeriod", this.periodInfo).then((res) => {
                    if (res.data.code === 20031) {
                        this.cronCreateFormVisible = false;
                        this.$message.success("执行周期设置成功");
                    } else if (res.data.code === 20030) {
                        this.$message.error("执行周期设置失败");
                    } else {
                        this.$message.error(res.data.msg);
                    }
                }).finally(() => {
                    this.getAll();
                });
            },

            //提取用户输入的邮箱地址
            getMailAddress(mailAddress) {
                let tempParam = "";
                const addrList = mailAddress.match(/[A-z0-9._]+@[A-z0-9._]+\.[A-z]+/g);
                if (addrList) {
                    for (let i = 0; i < addrList.length; i++) {
                        tempParam += addrList[i] + ";\n";
                    }
                    this.addTaskInfo.mailTask.recipient_mail_address = tempParam;
                } else if (addrList == null && mailAddress !== "") {
                    this.addTaskInfo.mailTask.recipient_mail_address = "";
                    this.$message.warning("请输入合法的邮箱地址！");
                }
            },
            getCcMailAddress(mailAddress) {
                let tempParam = "";
                const addrList = mailAddress.match(/[A-z0-9._]+@[A-z0-9._]+\.[A-z]+/g);
                if (addrList != null) {
                    for (let i = 0; i < addrList.length; i++) {
                        tempParam += addrList[i] + ";\n";
                    }
                    this.addTaskInfo.mailTask.recipient_cc_mail_address = tempParam;
                } else if (addrList == null && mailAddress !== "") {
                    this.$message.info("未检测到合法邮箱地址。");
                    this.addTaskInfo.mailTask.recipient_cc_mail_address = "";
                }
            },
            getEditMailAddress(mailAddress) {
                let tempParam = "";
                const addrList = mailAddress.match(/[A-z0-9._]+@[A-z0-9._]+\.[A-z]+/g);
                if (addrList) {
                    for (let i = 0; i < addrList.length; i++) {
                        tempParam += addrList[i] + ";\n";
                    }
                    this.taskInfo.mailTask.recipient_mail_address = tempParam;
                } else if (addrList == null && mailAddress !== "") {
                    this.taskInfo.mailTask.recipient_mail_address = "";
                    this.$message.warning("请输入合法的邮箱地址！");
                }
            },
            getEditCcMailAddress(mailAddress) {
                let tempParam = "";
                const addrList = mailAddress.match(/[A-z0-9._]+@[A-z0-9._]+\.[A-z]+/g);
                if (addrList != null) {
                    for (let i = 0; i < addrList.length; i++) {
                        tempParam += addrList[i] + ";\n";
                    }
                    this.taskInfo.mailTask.recipient_cc_mail_address = tempParam;
                } else if (addrList == null && mailAddress !== "") {
                    this.$message.info("未检测到合法邮箱地址。");
                    this.taskInfo.mailTask.recipient_cc_mail_address = "";
                }
            },

            // ftp连接测试
            ftpConnectCheck() {
                if (!this.taskInfo.task.task_name || !this.taskInfo.ftpTask.ftp_host || !this.taskInfo.ftpTask.ftp_port ||
                    !this.taskInfo.ftpTask.ftp_pwd || !this.taskInfo.ftpTask.ftp_user) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.$axios.post("/taskCheck/ftpCheck", this.taskInfo.ftpTask).then((res) => {
                    if (res.data.data === "success")
                        this.$message.success("ftp连接成功");
                    else
                        this.$message.error(res.data.data);
                }).finally(() => {
                });
            },
            addFtpConnectCheck() {
                if (!this.addTaskInfo.task.task_name || !this.addTaskInfo.ftpTask.ftp_host || !this.addTaskInfo.ftpTask.ftp_port ||
                    !this.addTaskInfo.ftpTask.ftp_pwd || !this.addTaskInfo.ftpTask.ftp_user) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                    return
                }
                this.$axios.post("/taskCheck/ftpCheck", this.addTaskInfo.ftpTask).then((res) => {
                    if (res.data.data === "success")
                        this.$message.success("ftp连接成功");
                    else
                        this.$message.error(res.data.data);
                }).finally(() => {
                });
            },

            // sql运行测试
            sqlCheck() {
                if (!this.taskSql.database_name || !this.taskSql.sql_text) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                } else {
                    this.$axios.post("/taskCheck/sqlCheck", this.taskSql).then((res) => {
                        if (res.data.data)      // 脚本运行成功则返回null，失败则返回失败原因
                            this.$message.error(res.data.data);
                        else
                            this.$message.success("脚本运行通过");

                    }).finally(() => {
                    });
                }
            },
            addSqlCheck() {
                if (!this.addTaskSql.database_name || !this.addTaskSql.sql_text) {
                    this.$message({message: '请完成必填项的输入！', type: 'warning'});
                } else {
                    this.$axios.post("/taskCheck/sqlCheck", this.addTaskSql).then((res) => {
                        if (res.data.data)      // 脚本运行成功则返回null，失败则返回失败原因
                            this.$message.error(res.data.data);
                        else
                            this.$message.success("脚本运行通过");
                    }).finally(() => {
                    });
                }
            },

            //改变任务状态
            changeTaskStatus(index, taskId, taskStatus) {
                this.statusInfo.task_id = taskId;
                this.statusInfo.task_status = taskStatus;
                if (taskStatus === 0) {
                    this.$axios.put("/taskInfo/taskStatus", this.statusInfo).then((res) => {
                        if (res.data.code === 20031) {
                            this.$message.info("任务关闭");
                        } else if (res.data.code === 20030) {
                            this.$message.error("修改失败");
                        } else {
                            this.$message.error(res.data.msg);
                        }
                    });
                } else {
                    this.$axios.get("/taskCheck/taskIntegrity/" + taskId).then((res) => {
                        if (res.data.code === 20041 && res.data.data === 10) {
                            this.$axios.put("/taskInfo/taskStatus", this.statusInfo).then((res) => {
                                if (res.data.code === 20031) {
                                    this.$message.success("任务开启");
                                } else if (res.data.code === 20030) {
                                    this.$message.error("修改失败");
                                } else {
                                    this.$message.error(res.data.msg);
                                }
                            });
                        } else if (res.data.code === 20040) {
                            this.$message.error("开启失败");
                        } else if (res.data.code === 20041 && res.data.data === 1) {
                            this.$message.warning("任务执行周期未设置，任务不能开启！");
                            this.dataList[index]["task_status"] = 0;
                        } else if (res.data.code === 20041 && res.data.data === 2) {
                            this.$message.warning("任务结果文件未添加，任务不能开启！");
                            this.dataList[index]["task_status"] = 0;
                        } else if (res.data.code === 20041 && res.data.data === 3) {
                            this.$message.warning("有结果文件没有添加任务sql，任务不能开启！");
                            this.dataList[index]["task_status"] = 0;
                        }
                    })
                }
            }
        }
    }
</script>

<style scoped>
    @import "../assets/style.css";
</style>