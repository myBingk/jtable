
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS "adapter";
CREATE TABLE "adapter" (
  "adapterId" bigint(20) NOT NULL,
  "systemId" bigint(20) DEFAULT NULL COMMENT '系统id',
  "tableName" varchar(30) NOT NULL COMMENT '要适配的表',
  "propertyBigint1" bigint(20) DEFAULT NULL,
  "propertyBigint2" bigint(20) DEFAULT NULL,
  "propertyInt1" int(9) DEFAULT NULL,
  "propertyInt2" int(9) DEFAULT NULL,
  "propertyVarchar1" varchar(150) DEFAULT NULL,
  "propertyVarchar2" varchar(225) DEFAULT NULL,
  "propertyVarchar3" varchar(225) DEFAULT NULL,
  "propertyVarchar4" varchar(300) DEFAULT NULL,
  "propertyDecimal1" decimal(12,2) DEFAULT NULL,
  "propertyDecimal2" decimal(20,4) DEFAULT NULL,
  "propertyTime1" datetime DEFAULT NULL,
  "propertyTime2" datetime DEFAULT NULL,
  "propertyText1" text,
  PRIMARY KEY ("adapterId"),
  KEY "idx_adapter_systemId" ("systemId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "agent_account";
CREATE TABLE "agent_account" (
  "accountId" bigint(20) NOT NULL,
  "agentId" bigint(20) NOT NULL,
  "balance" decimal(10,2) DEFAULT NULL COMMENT '余额',
  "lockedBalance" decimal(10,2) DEFAULT NULL COMMENT '锁定金额',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "status" int(1) NOT NULL COMMENT '状态',
  PRIMARY KEY ("accountId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "agent_account_flow";
CREATE TABLE "agent_account_flow" (
  "accountFlowId" bigint(20) NOT NULL,
  "accountId" bigint(20) NOT NULL COMMENT '账户id',
  "agentId" bigint(20) NOT NULL COMMENT '代理商id',
  "category" int(1) DEFAULT NULL COMMENT '分类',
  "type" int(1) NOT NULL COMMENT '类别（主要是收入和支出）',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '金额',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("accountFlowId"),
  KEY "idx_agent_account_flow_agentId" ("agentId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "agent_distribution";
CREATE TABLE "agent_distribution" (
  "distributionId" bigint(20) NOT NULL,
  "agentId" bigint(20) NOT NULL COMMENT '代理商id',
  "parentAgentId" bigint(20) NOT NULL COMMENT '父id',
  "creationTime" datetime NOT NULL,
  PRIMARY KEY ("distributionId"),
  UNIQUE KEY "uk_agent_distribution_agentId_parentAgentId" ("agentId","parentAgentId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "agent_info";
CREATE TABLE "agent_info" (
  "agentId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(50) DEFAULT NULL COMMENT '名称',
  "abbreviation" varchar(10) DEFAULT NULL COMMENT '简称',
  "linkman" varchar(10) DEFAULT NULL COMMENT '联系人',
  "mobilePhone" varchar(11) NOT NULL COMMENT '手机',
  "telephone" varchar(20) DEFAULT NULL COMMENT '电话',
  "email" varchar(50) DEFAULT NULL COMMENT '邮箱',
  "type" int(1) DEFAULT NULL COMMENT '类型',
  "city" varchar(20) NOT NULL COMMENT '城市',
  "address" varchar(100) DEFAULT NULL COMMENT '地址',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "auditInformation" bigint(20) DEFAULT NULL COMMENT '审核资料',
  "status" int(1) NOT NULL COMMENT '状态',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  "userId" bigint(20) DEFAULT NULL COMMENT '用户id',
  "invitationCode" varchar(8) DEFAULT NULL COMMENT '邀请码',
  PRIMARY KEY ("agentId"),
  UNIQUE KEY "uk_member_info_userId" ("userId") USING BTREE,
  KEY "idx_agent_info_city" ("city") USING BTREE,
  KEY "idx_agent_info_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "assessment_data";
CREATE TABLE "assessment_data" (
  "dataId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "assessmentId" bigint(20) NOT NULL COMMENT '考评信息id',
  "departmentId" bigint(20) NOT NULL COMMENT '部门id',
  "formId" bigint(20) NOT NULL COMMENT '考评表id',
  "content" text COMMENT '考评数据（考评表内容）',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "status" int(1) NOT NULL COMMENT '状态',
  "isDeleted" boolean NOT NULL COMMENT '是否删除',
  "reviewTime" int(1) NOT NULL DEFAULT '0' COMMENT '审核次数',
  "nextNode" varchar(30) DEFAULT NULL COMMENT '下一节点（业务流程中节点）',
  "score" decimal(4,1) DEFAULT NULL COMMENT '最终得分（考评办审核后得出）',
  "creator" bigint(20) NOT NULL,
  "updator" bigint(20) NOT NULL,
  "flowId" bigint(20) DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY ("dataId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "assessment_data_review";
CREATE TABLE "assessment_data_review" (
  "reviewId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "dataId" bigint(20) NOT NULL COMMENT '考评数据id',
  "remark" varchar(300) DEFAULT NULL COMMENT '备注',
  "result" int(1) NOT NULL COMMENT '审核结果（对应考评数据表status）',
  "reviewer" bigint(20) NOT NULL COMMENT '审核人',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("reviewId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "assessment_form_department";
CREATE TABLE "assessment_form_department" (
  "assessmentId" bigint(20) NOT NULL COMMENT '考评信息id',
  "formId" bigint(20) NOT NULL COMMENT '考评表id',
  "departmentId" bigint(20) NOT NULL COMMENT '部门id',
  PRIMARY KEY ("assessmentId","formId","departmentId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "assessment_info";
CREATE TABLE "assessment_info" (
  "assessmentId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(80) DEFAULT NULL COMMENT '考评名称',
  "startDate" date DEFAULT NULL COMMENT '开始时间',
  "endDate" date DEFAULT NULL COMMENT '结束时间',
  "description" varchar(500) DEFAULT NULL COMMENT '描述',
  "creationTime" datetime DEFAULT NULL,
  "updateTime" datetime DEFAULT NULL,
  "status" int(1) DEFAULT NULL COMMENT '状态（草稿，已发布，已关闭，已结束）',
  "isDeleted" boolean DEFAULT NULL COMMENT '是否被删除',
  "resultStatus" int(1) DEFAULT NULL COMMENT '结果状态（草稿，已发布，已撤回）',
  PRIMARY KEY ("assessmentId"),
  KEY "idx_assessment_info_systemId" ("systemId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "assessment_rank";
CREATE TABLE "assessment_rank" (
  "rankId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "assessmentId" bigint(20) NOT NULL COMMENT '考评信息id',
  "departmentId" bigint(20) NOT NULL COMMENT '部门id',
  "dataId" bigint(20) NOT NULL COMMENT '考评数据id',
  "category" varchar(30) NOT NULL COMMENT '考评结果分类/奖次Title',
  "rank" int(3) DEFAULT NULL COMMENT '排名',
  "grading" bigint(20) NOT NULL COMMENT '等次',
  "score" decimal(4,1) NOT NULL COMMENT '考评分数',
  "remark" varchar(300) DEFAULT NULL COMMENT '备注',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  PRIMARY KEY ("rankId"),
  KEY "idx_assessment_rank_assessmentId" ("assessmentId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "basic_dictionary";
CREATE TABLE "basic_dictionary" (
  "dictionaryId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(50) NOT NULL COMMENT '显示名称',
  "value" varchar(200) DEFAULT NULL COMMENT '值',
  "code" varchar(20) NOT NULL COMMENT '编码',
  "description" varchar(50) DEFAULT NULL COMMENT '描述',
  "parentId" bigint(20) DEFAULT NULL COMMENT '父id',
  "orderBy" int(10) DEFAULT NULL COMMENT '排序',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("dictionaryId"),
  UNIQUE KEY "uk_code" ("code") USING BTREE,
  KEY "idx_basic_dictionary_systemId" ("systemId") USING BTREE,
  KEY "idx_basic_dictionary_parentId" ("parentId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "basic_protocol";
CREATE TABLE "basic_protocol" (
  "protocolId" bigint(20) NOT NULL COMMENT '主键',
  "systemId" bigint(19) NOT NULL COMMENT '系统id',
  "content" longtext COMMENT '内容',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '修改时间',
  "updator" bigint(20) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY ("protocolId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "basic_question_and_answer";
CREATE TABLE "basic_question_and_answer" (
  "questionAndAnswerId" bigint(19) NOT NULL,
  "systemId" bigint(19) NOT NULL,
  "question" varchar(40) NOT NULL COMMENT '问题',
  "answer" text NOT NULL COMMENT '回答',
  "type" int(1) NOT NULL DEFAULT '0' COMMENT '类型',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '修改时间',
  "isDeleted" boolean NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY ("questionAndAnswerId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "basic_resources";
CREATE TABLE "basic_resources" (
  "resourcesId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "groupId" bigint(20) NOT NULL COMMENT '组id',
  "url" varchar(300) DEFAULT NULL COMMENT '资源路径',
  "type" int(1) DEFAULT NULL COMMENT '资源类型',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY ("resourcesId"),
  KEY "idx_basic_resources_groupId_systemId" ("groupId","systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "basic_system";
CREATE TABLE "basic_system" (
  "systemId" bigint(20) NOT NULL,
  "code" varchar(10) NOT NULL COMMENT '系统代码',
  "name" varchar(30) NOT NULL COMMENT '系统名',
  "icon" varchar(255) DEFAULT NULL COMMENT '系统图标',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY ("systemId"),
  UNIQUE KEY "uk_basic_system_code" ("code") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "basic_system_variable";
CREATE TABLE "basic_system_variable" (
  "systemVariableId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "title" varchar(50) DEFAULT NULL COMMENT '标题',
  "name" varchar(50) NOT NULL COMMENT '键',
  "value" varchar(255) NOT NULL COMMENT '值',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("systemVariableId"),
  UNIQUE KEY "uk_basic_system_variable_systemId_name" ("systemId","name") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "basic_variable";
CREATE TABLE "basic_variable" (
  "variableId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "tableName" varchar(30) NOT NULL COMMENT '表名',
  "columnName" varchar(20) NOT NULL COMMENT '列名',
  "title" varchar(20) NOT NULL COMMENT '显示标题',
  "value" varchar(50) NOT NULL COMMENT '值',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("variableId"),
  KEY "idx_basic_variable_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "content_advertising";
CREATE TABLE "content_advertising" (
  "advertisingId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "title" varchar(20) NOT NULL COMMENT '标题',
  "picture" bigint(20) DEFAULT NULL COMMENT '图片id',
  "linkType" int(11) NOT NULL COMMENT '链接类型（站内链接，网页链接）',
  "appType" varchar(20) DEFAULT NULL COMMENT '应用类型（应用在哪个小程序）',
  "link" varchar(225) DEFAULT NULL COMMENT '链接',
  "status" int(1) NOT NULL COMMENT '状态（新增，发布，撤回等）',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL COMMENT '是否删除。',
  PRIMARY KEY ("advertisingId"),
  KEY "idx_content_advertising_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "date_default_record";
CREATE TABLE "date_default_record" (
  "defaultRecordId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL COMMENT '违约会员id',
  "mergeId" bigint(20) NOT NULL COMMENT '约会拼单id',
  "reason" varchar(50) DEFAULT NULL COMMENT '违约理由/原因',
  "status" int(1) NOT NULL COMMENT '状态',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "sponsor" bigint(20) NOT NULL COMMENT '发起人（会员id）',
  PRIMARY KEY ("defaultRecordId"),
  KEY "idx_date_default_record_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "date_invitation";
CREATE TABLE "date_invitation" (
  "invitationId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "mergeId" bigint(20) NOT NULL COMMENT '拼单id',
  "memberId" bigint(20) NOT NULL COMMENT '邀约用户id',
  "title" varchar(30) DEFAULT NULL COMMENT '标题',
  "picture" bigint(20) DEFAULT NULL COMMENT '图片',
  "video" bigint(20) DEFAULT NULL COMMENT '视频',
  "accurateTime" datetime DEFAULT NULL COMMENT '准确时间（和时间段2选一）',
  "leftTime" datetime DEFAULT NULL COMMENT '时间段（和准确时间2选1）',
  "rightTime" datetime DEFAULT NULL COMMENT '时间段（和准确时间2选1）',
  "feeRatio" int(3) DEFAULT NULL COMMENT '费用占比',
  "fee" decimal(10,2) DEFAULT NULL COMMENT '应付费用',
  "status" int(11) NOT NULL COMMENT '状态（邀约记录状态）',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  "isFree" boolean NOT NULL,
  PRIMARY KEY ("invitationId"),
  UNIQUE KEY "uk_date_invitation_mergeId" ("mergeId") USING BTREE,
  UNIQUE KEY "uk_date_invitation_memberId_accurateTime" ("memberId","accurateTime") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "date_merge";
CREATE TABLE "date_merge" (
  "mergeId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "merchantId" bigint(20) NOT NULL COMMENT '商家id',
  "packageId" bigint(20) NOT NULL COMMENT '商品id',
  "packageName" varchar(20) DEFAULT NULL COMMENT '商品名',
  "packagePrice" decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  "packagePictureUrl" varchar(300) DEFAULT NULL COMMENT '商品图片',
  "totalFee" decimal(10,2) DEFAULT NULL COMMENT '总价',
  "status" int(1) NOT NULL COMMENT '状态',
  "accurateTime" datetime DEFAULT NULL COMMENT '确定时间',
  "isDeleted" boolean NOT NULL,
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("mergeId"),
  KEY "idx_date_merge_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "date_response";
CREATE TABLE "date_response" (
  "responseId" bigint(20) NOT NULL,
  "invitationId" bigint(20) NOT NULL COMMENT '邀请记录id',
  "mergeId" bigint(20) DEFAULT NULL COMMENT '拼单id',
  "memberId" bigint(20) NOT NULL COMMENT '应约用户id',
  "remark" varchar(30) DEFAULT NULL COMMENT '备注',
  "accurateTime" datetime NOT NULL COMMENT '准确时间',
  "feeRatio" int(3) DEFAULT NULL COMMENT '费用占比',
  "fee" decimal(10,2) DEFAULT NULL COMMENT '应付费用',
  "status" int(1) NOT NULL COMMENT '状态（违规屏蔽，等待回应，应答成功....）',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  "isFree" boolean NOT NULL,
  PRIMARY KEY ("responseId"),
  UNIQUE KEY "uk_date_response_mergeId" ("mergeId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "date_sign";
CREATE TABLE "date_sign" (
  "signId" bigint(20) NOT NULL,
  "mergeId" bigint(20) NOT NULL COMMENT '拼单id',
  "memberId" bigint(20) NOT NULL COMMENT '会员id',
  "type" int(1) NOT NULL COMMENT '类型（应约者，邀约者）',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("signId"),
  UNIQUE KEY "uk_date_sign_mergeId_memberId" ("mergeId","memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "member_account";
CREATE TABLE "member_account" (
  "accountId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "balance" decimal(10,2) DEFAULT NULL COMMENT '余额',
  "lockedBalance" decimal(10,2) DEFAULT NULL COMMENT '锁定余额',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "status" int(1) NOT NULL COMMENT '状态',
  PRIMARY KEY ("accountId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "member_account_flow";
CREATE TABLE "member_account_flow" (
  "accountFlowId" bigint(20) NOT NULL,
  "accountId" bigint(20) NOT NULL COMMENT '账户id',
  "memberId" bigint(20) NOT NULL COMMENT '用户id',
  "category" int(1) DEFAULT NULL COMMENT '分类',
  "type" int(1) NOT NULL COMMENT '类别（主要是收入和支出）',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '金额',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("accountFlowId"),
  KEY "idx_member_account_flow_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "member_address";
CREATE TABLE "member_address" (
  "addressId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "linkman" varchar(30) NOT NULL,
  "phone" varchar(20) NOT NULL,
  "address" varchar(250) NOT NULL,
  "city" varchar(20) NOT NULL,
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  "isDefault" int(11) NOT NULL,
  PRIMARY KEY ("addressId"),
  KEY "idx_member_address_memberId_systemId" ("memberId","systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "member_config";
CREATE TABLE "member_config" (
  "configId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "name" varchar(255) NOT NULL COMMENT '配置键',
  "value" varchar(255) NOT NULL COMMENT '配置值',
  "remark" varchar(255) DEFAULT NULL COMMENT '备注',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("configId"),
  KEY "idx_member_config_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "member_distribution";
CREATE TABLE "member_distribution" (
  "distributionId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "parentMemberId" bigint(20) NOT NULL,
  "creationTime" datetime NOT NULL,
  PRIMARY KEY ("distributionId"),
  UNIQUE KEY "uk_member_distribution_memberId_parentMemberId" ("memberId","parentMemberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "member_info";
CREATE TABLE "member_info" (
  "memberId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(20) DEFAULT NULL COMMENT '昵称',
  "sex" int(1) DEFAULT NULL COMMENT '性别',
  "age" int(3) DEFAULT NULL COMMENT '年龄',
  "birthday" date DEFAULT NULL COMMENT '生日',
  "phone" varchar(11) DEFAULT NULL COMMENT '手机号码',
  "email" varchar(50) DEFAULT NULL COMMENT '邮箱',
  "city" varchar(20) DEFAULT NULL COMMENT '城市',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "userId" bigint(20) NOT NULL COMMENT '用户id',
  "type" int(1) DEFAULT NULL COMMENT '会员类型',
  "avatar" bigint(20) DEFAULT NULL COMMENT '头像',
  "invitationCode" varchar(8) NOT NULL COMMENT '邀请码',
  "registration" varchar(20) DEFAULT NULL COMMENT '注册地',
  "remark" varchar(150) DEFAULT NULL COMMENT '个人简介',
  PRIMARY KEY ("memberId"),
  UNIQUE KEY "uk_member_infouserId" ("userId") USING BTREE,
  UNIQUE KEY "uk_systemId_invitationCode" ("systemId","invitationCode") USING BTREE,
  KEY "idx_member_info_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "member_integral";
CREATE TABLE "member_integral" (
  "integralId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "number" int(11) DEFAULT NULL COMMENT '积分数',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("integralId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "member_integral_flow";
CREATE TABLE "member_integral_flow" (
  "integralFlowId" bigint(20) NOT NULL,
  "integralId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "remark" varchar(20) DEFAULT NULL,
  "type" int(1) NOT NULL,
  "number" int(11) DEFAULT NULL,
  "creationTime" datetime NOT NULL,
  PRIMARY KEY ("integralFlowId"),
  KEY "idx_member_integral_flow_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "member_subscription";
CREATE TABLE "member_subscription" (
  "subscriptionId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL COMMENT '会员id',
  "subscribeMemberId" bigint(20) NOT NULL COMMENT '订阅的目标会员id',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "isDeleted" boolean NOT NULL,
  PRIMARY KEY ("subscriptionId"),
  KEY "idx_member_subscription_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "member_third";
CREATE TABLE "member_third" (
  "thirdId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL COMMENT '会员id',
  "type" int(1) NOT NULL COMMENT '第三方类型（微信，QQ等）',
  "openId" varchar(50) NOT NULL COMMENT '第三方openid',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("thirdId"),
  UNIQUE KEY "uk_member_thirdopenId" ("openId") USING BTREE,
  KEY "idx_member_third_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "member_user";
CREATE TABLE "member_user" (
  "userId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(30) DEFAULT NULL COMMENT '登陆用户名',
  "password" varchar(255) DEFAULT NULL COMMENT '密码',
  "salt" varchar(255) DEFAULT NULL COMMENT '密码盐',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "status" int(1) DEFAULT NULL COMMENT '状态',
  "openId" varchar(200) DEFAULT NULL COMMENT '第三方登陆id',
  PRIMARY KEY ("userId"),
  UNIQUE KEY "uk_member_user_name_systemId" ("name","systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "member_vip_commission_config";
CREATE TABLE "member_vip_commission_config" (
  "configId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "level" int(1) NOT NULL COMMENT '返佣级别（0级代表自己，1级为父级...类推）',
  "proportion" decimal(4,1) DEFAULT NULL COMMENT '百分比例',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '固定数值',
  "type" int(1) NOT NULL COMMENT '代理会员/代理商',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  PRIMARY KEY ("configId"),
  KEY "idx_member_vip_commission_config_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "member_vip_commission_flow";
CREATE TABLE "member_vip_commission_flow" (
  "id" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) DEFAULT NULL COMMENT '得到返佣会员id',
  "agentId" bigint(20) DEFAULT NULL COMMENT '得到返佣代理商id',
  "accountId" bigint(20) DEFAULT NULL COMMENT '返佣账户id',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '返佣金额',
  "creationTime" datetime NOT NULL,
  "vipOrderId" bigint(20) NOT NULL COMMENT '购买vip的订单id',
  "level" int(1) DEFAULT NULL COMMENT '级别',
  PRIMARY KEY ("id"),
  KEY "idx_member_vip_commission_flow_memberId" ("memberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "member_vip_distribution";
CREATE TABLE "member_vip_distribution" (
  "distributionId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "parentMemberId" bigint(20) NOT NULL,
  "creationTime" datetime NOT NULL,
  PRIMARY KEY ("distributionId"),
  UNIQUE KEY "uk_member_vip_distribution_memberId_parentMemberId" ("memberId","parentMemberId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "member_vip_order";
CREATE TABLE "member_vip_order" (
  "vipOrderId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL COMMENT '会员id',
  "payment" decimal(10,2) NOT NULL COMMENT '实付金额',
  "paymentWay" int(1) NOT NULL COMMENT '支付方式',
  "payTime" datetime DEFAULT NULL COMMENT '支付时间',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "tradeNo" varchar(50) DEFAULT NULL COMMENT '第三方交易流水号',
  "endTime" datetime DEFAULT NULL COMMENT '会员截止时间',
  PRIMARY KEY ("vipOrderId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "merchant_account";
CREATE TABLE "merchant_account" (
  "accountId" bigint(20) NOT NULL,
  "merchantId" bigint(20) NOT NULL,
  "balance" decimal(10,2) DEFAULT NULL COMMENT '余额',
  "lockedBalance" decimal(10,2) DEFAULT NULL COMMENT '锁定金额',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "status" int(11) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY ("accountId"),
  UNIQUE KEY "uk_merchant_account_merchantId" ("merchantId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "merchant_account_flow";
CREATE TABLE "merchant_account_flow" (
  "accountFlowId" bigint(20) NOT NULL,
  "accountId" bigint(20) NOT NULL COMMENT '账户id',
  "merchantId" bigint(20) NOT NULL COMMENT '商户id',
  "category" int(1) DEFAULT NULL COMMENT '分类',
  "type" int(1) NOT NULL COMMENT '类别（主要是收入和支出）',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '金额',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("accountFlowId"),
  KEY "idx_merchant_account_flow_merchantId" ("merchantId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "merchant_info";
CREATE TABLE "merchant_info" (
  "merchantId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(50) NOT NULL COMMENT '商家名称',
  "abbreviation" varchar(10) DEFAULT NULL COMMENT '商家简称',
  "type" int(1) unsigned NOT NULL COMMENT '商家类型',
  "linkman" varchar(10) DEFAULT NULL COMMENT '联系人',
  "mobilePhone" varchar(11) NOT NULL COMMENT '手机',
  "telephone" varchar(20) DEFAULT NULL COMMENT '固定电话',
  "email" varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  "logo" bigint(20) DEFAULT NULL COMMENT '商家logo',
  "status" int(1) DEFAULT NULL COMMENT '状态',
  "city" varchar(20) NOT NULL COMMENT '地区编码',
  "businessLicense" bigint(20) DEFAULT NULL COMMENT '营业执照',
  "perCapitaPrice" decimal(10,2) DEFAULT NULL COMMENT '人均价格',
  "address" varchar(100) NOT NULL COMMENT '地址',
  "longitude" decimal(16,13) DEFAULT NULL COMMENT '经度',
  "latitude" decimal(15,13) DEFAULT NULL COMMENT '纬度',
  "agentId" bigint(20) unsigned DEFAULT NULL COMMENT '代理商id',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "userId" bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY ("merchantId"),
  UNIQUE KEY "uk_merchant_info_userId" ("userId") USING BTREE,
  KEY "idx_merchant_info_systemId" ("systemId") USING BTREE,
  KEY "idx_merchant_info_agentId" ("agentId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "merchant_package";
CREATE TABLE "merchant_package" (
  "packageId" bigint(20) NOT NULL,
  "merchantId" bigint(20) NOT NULL,
  "name" varchar(20) NOT NULL COMMENT '名称',
  "introduction" varchar(30) DEFAULT NULL COMMENT '简介',
  "price" decimal(10,2) NOT NULL COMMENT '单价',
  "picture" bigint(20) DEFAULT NULL COMMENT '图片',
  "detail" text COMMENT '详情',
  "creationTime" datetime DEFAULT NULL COMMENT '创建时间',
  "updateTime" datetime DEFAULT NULL COMMENT '更新时间',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  "status" int(1) DEFAULT NULL COMMENT '状态（草稿，上架，下架。。。）',
  PRIMARY KEY ("packageId"),
  KEY "idx_merchant_packagemerchantId" ("merchantId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "merchant_third";
CREATE TABLE "merchant_third" (
  "thirdId" bigint(20) NOT NULL,
  "merchantId" bigint(20) NOT NULL COMMENT '会员id',
  "type" int(1) NOT NULL COMMENT '第三方类型（微信，QQ等）',
  "openId" varchar(50) NOT NULL COMMENT '第三方openid',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY ("thirdId"),
  UNIQUE KEY "uk_merchant_thirdopenId" ("openId") USING BTREE,
  KEY "idx_merchant_thirdmemberId" ("merchantId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "notice_channel";
CREATE TABLE "notice_channel" (
  "channelId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(30) DEFAULT NULL COMMENT '频道名称',
  "picture" bigint(20) DEFAULT NULL COMMENT '图标',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "status" int(1) DEFAULT NULL COMMENT '状态',
  "isDeleted" boolean NOT NULL COMMENT '删除',
  PRIMARY KEY ("channelId"),
  KEY "idx_notice_channelsystemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "notice_member_news";
CREATE TABLE "notice_member_news" (
  "newsId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL,
  "channelId" bigint(20) DEFAULT NULL COMMENT '频道id',
  "content" varchar(300) DEFAULT NULL COMMENT '内容',
  "isRead" boolean DEFAULT NULL COMMENT '是否已读',
  "targetUrl" varchar(200) DEFAULT NULL COMMENT '目标链接',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY ("newsId"),
  KEY "idx_notice_member_newsmemberId_isRead" ("memberId","isRead") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "pending_advice";
CREATE TABLE "pending_advice" (
  "adviceId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) DEFAULT NULL COMMENT '会员id',
  "merchantId" bigint(20) DEFAULT NULL COMMENT '商户id',
  "type" int(1) NOT NULL COMMENT '类型（bug反馈，产品建议。。。）',
  "content" varchar(500) NOT NULL COMMENT '内容正文',
  "picture" bigint(20) DEFAULT NULL COMMENT '附件图片',
  "cellphone" varchar(20) DEFAULT NULL COMMENT '联系手机',
  "email" varchar(50) DEFAULT NULL COMMENT '联系邮箱',
  "creationTime" datetime DEFAULT NULL,
  "updateTime" datetime DEFAULT NULL,
  PRIMARY KEY ("adviceId"),
  KEY "idx_pending_advicesystemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "pending_violation";
CREATE TABLE "pending_violation" (
  "violationId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) DEFAULT NULL COMMENT '会员id（发起人/举报人）',
  "targetId" bigint(20) NOT NULL COMMENT '对象id、目标id',
  "type" int(1) DEFAULT NULL COMMENT '类型（举报会员、举报帖子）',
  "annex" bigint(20) DEFAULT NULL COMMENT '截图/图片附件',
  "reason" varchar(255) DEFAULT NULL COMMENT '理由',
  "status" int(1) NOT NULL COMMENT '状态（审核、未审核。。）',
  "processingType" int(1) DEFAULT NULL COMMENT '处理类型',
  "processingResult" varchar(150) DEFAULT NULL COMMENT '处理结果',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("violationId")
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "service_info";
CREATE TABLE "service_info" (
  "serviceId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(30) DEFAULT NULL COMMENT '服务名称',
  "price" decimal(10,2) DEFAULT NULL COMMENT '服务价格',
  "detail" text COMMENT '服务详情/介绍',
  "status" int(1) NOT NULL COMMENT '服务状态（新增，上架，下架）',
  "isDeleted" boolean NOT NULL,
  "picture" bigint(20) DEFAULT NULL COMMENT '服务图片。',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("serviceId"),
  KEY "idx_service_infosystemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "service_staff";
CREATE TABLE "service_staff" (
  "staffId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "serviceId" bigint(20) NOT NULL COMMENT '服务id',
  "name" varchar(10) NOT NULL COMMENT '员工姓名',
  "age" int(3) DEFAULT NULL COMMENT '年龄',
  "remark" varchar(300) DEFAULT NULL COMMENT '备注',
  "picture" bigint(20) DEFAULT NULL COMMENT '照片',
  "serviceArea" varchar(150) DEFAULT NULL COMMENT '服务区域（区域编码的集合）',
  "address" varchar(150) DEFAULT NULL COMMENT '地址',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "price" decimal(10,2) DEFAULT NULL COMMENT '服务价格',
  PRIMARY KEY ("staffId"),
  KEY "idx_service_staffserviceId_systemId" ("serviceId","systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu" (
  "menuId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL COMMENT '系统id',
  "name" varchar(10) DEFAULT NULL COMMENT '菜单名称',
  "type" int(1) NOT NULL COMMENT '菜单类型（目录，菜单，按钮）',
  "url" varchar(200) DEFAULT NULL COMMENT '菜单路径',
  "permissionValue" varchar(50) NOT NULL COMMENT '权限值',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  "icon" varchar(50) DEFAULT NULL COMMENT '图标',
  "parentId" bigint(20) DEFAULT NULL COMMENT '父菜单id',
  PRIMARY KEY ("menuId"),
  UNIQUE KEY "uk_sys_menusystemId_permissionValue" ("systemId","permissionValue") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role" (
  "roleId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "name" varchar(20) NOT NULL COMMENT '角色名（可为中文，用于页面显示）',
  "value" varchar(20) NOT NULL COMMENT '关键字（英文，用来判断具体的角色）',
  "description" varchar(50) DEFAULT NULL COMMENT '描述',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "creator" bigint(20) DEFAULT NULL,
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "updater" bigint(20) DEFAULT NULL,
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除',
  PRIMARY KEY ("roleId"),
  UNIQUE KEY "uk_sys_rolesystemId__value_name" ("systemId","value","name") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu" (
  "id" bigint(20) NOT NULL,
  "roleId" bigint(20) NOT NULL,
  "menuId" bigint(20) NOT NULL,
  PRIMARY KEY ("id"),
  UNIQUE KEY "uk_sys_role_menuroleId_menuId" ("roleId","menuId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user" (
  "userId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "userName" varchar(50) NOT NULL COMMENT '登陆用户名',
  "password" varchar(255) DEFAULT NULL COMMENT '登陆密码',
  "salt" varchar(32) NOT NULL COMMENT '密码的盐值',
  "name" varchar(30) DEFAULT NULL COMMENT '姓名/昵称',
  "phone" varchar(20) DEFAULT NULL COMMENT '电话号码',
  "email" varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  "type" int(1) unsigned DEFAULT NULL COMMENT '类型(用户类型)',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "creator" bigint(20) DEFAULT NULL COMMENT '创建人',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "updater" bigint(20) DEFAULT NULL COMMENT '更新人',
  "status" int(1) NOT NULL COMMENT '状态',
  "isDeleted" boolean NOT NULL COMMENT '逻辑删除（0未删除，1删除）',
  "roleId" bigint(20) DEFAULT NULL COMMENT '角色id',
  "organizationStructure" varchar(255) DEFAULT NULL COMMENT '组织结构code',
  PRIMARY KEY ("userId"),
  UNIQUE KEY "uk_sys_usersystemId_userName" ("systemId","userName") USING BTREE,
  UNIQUE KEY "uk_sys_usersystemId_phone" ("systemId","phone") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role" (
  "id" bigint(20) NOT NULL,
  "userId" bigint(20) NOT NULL,
  "roleId" bigint(20) NOT NULL,
  PRIMARY KEY ("id"),
  UNIQUE KEY "uk_sys_user_roleuserId_roleId" ("userId","roleId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "trade_commission_config";
CREATE TABLE "trade_commission_config" (
  "configId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "level" int(1) NOT NULL COMMENT '返佣级别（0级代表自己，1级为父级...类推）',
  "proportion" decimal(4,1) DEFAULT NULL COMMENT '比例',
  "type" int(1) NOT NULL COMMENT '类型（普通会员，代理商，代理会员等）',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "isDeleted" boolean NOT NULL,
  PRIMARY KEY ("configId"),
  KEY "idx_trade_commission_configsystemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "trade_commission_flow";
CREATE TABLE "trade_commission_flow" (
  "commissionFlowId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) DEFAULT NULL COMMENT '返佣会员id',
  "merchantId" bigint(20) DEFAULT NULL COMMENT '返佣商户id',
  "agentId" bigint(20) DEFAULT NULL COMMENT '返佣代理商id',
  "accountId" bigint(20) DEFAULT NULL COMMENT '返佣账户id',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '金额',
  "isOpen" boolean DEFAULT NULL COMMENT '是否打开（红包用，未打开看不到金额）',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "orderId" bigint(20) DEFAULT NULL COMMENT '关联订单',
  "level" int(1) DEFAULT NULL COMMENT '返佣级别',
  PRIMARY KEY ("commissionFlowId"),
  KEY "idx_trade_commission_flowsystemId" ("systemId") USING BTREE,
  KEY "idx_trade_commission_flowmemberId" ("memberId") USING BTREE,
  KEY "idx_trade_commission_flowmerchantId" ("merchantId") USING BTREE,
  KEY "idx_trade_commission_flowagentId" ("agentId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS "trade_order";
CREATE TABLE "trade_order" (
  "orderId" bigint(20) NOT NULL,
  "memberId" bigint(20) NOT NULL COMMENT '会员id',
  "systemId" bigint(20) NOT NULL COMMENT '系统id',
  "merchantId" bigint(20) DEFAULT NULL COMMENT '商家id（订单按照商家拆单）',
  "payment" decimal(10,2) DEFAULT NULL COMMENT '实付金额',
  "paymentWay" int(1) DEFAULT NULL COMMENT '支付方式/渠道（支付宝，微信等）',
  "status" int(1) DEFAULT NULL COMMENT '状态',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  "payTime" datetime DEFAULT NULL COMMENT '付款时间',
  "isDeleted" boolean NOT NULL,
  "tradeNo" varchar(50) DEFAULT NULL COMMENT '第三方交易流水号',
  "isMerged" boolean NOT NULL COMMENT '是否是约会拼单单',
  "mergeId" bigint(20) DEFAULT NULL COMMENT '拼单id',
  "ownFeeRatio" int(3) DEFAULT NULL COMMENT '自费比例（两人或多人约单）',
  "serviceTime" datetime DEFAULT NULL COMMENT '预约时间',
  PRIMARY KEY ("orderId"),
  KEY "idx_trade_ordermergeId" ("mergeId") USING BTREE,
  KEY "idx_trade_ordermemberId" ("memberId") USING BTREE,
  KEY "idx_trade_ordersystemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS "trade_order_comment";
CREATE TABLE "trade_order_comment" (
  "commentId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "orderId" bigint(20) NOT NULL COMMENT '订单id',
  "goodsId" bigint(20) DEFAULT NULL COMMENT '评价商品id',
  "memberId" bigint(20) DEFAULT NULL COMMENT '评价人id',
  "star" int(1) DEFAULT NULL COMMENT '星级分数',
  "content" varchar(350) DEFAULT NULL COMMENT '评价内容',
  "isAnonymous" int(1) NOT NULL COMMENT '是否匿名',
  "isDeleted" boolean NOT NULL COMMENT '是否删除',
  "creationTime" datetime NOT NULL,
  "updateTime" datetime NOT NULL,
  PRIMARY KEY ("commentId"),
  KEY "idx_trade_order_commentsystemId_goodsId" ("systemId","goodsId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS "trade_order_item";
CREATE TABLE "trade_order_item" (
  "orderItemId" bigint(20) NOT NULL,
  "orderId" bigint(20) NOT NULL COMMENT '订单id',
  "goodsId" bigint(20) NOT NULL COMMENT '商品id',
  "goodsName" varchar(20) DEFAULT NULL COMMENT '商品名',
  "goodsPrice" decimal(10,2) DEFAULT NULL COMMENT '商品单价',
  "goodsPicture" varchar(300) DEFAULT NULL COMMENT '图片地址',
  "goodsNum" int(11) DEFAULT NULL COMMENT '商品数量',
  "totalFee" decimal(10,2) DEFAULT NULL COMMENT '总价',
  "StaffId" bigint(20) DEFAULT NULL COMMENT '服务人员id',
  "staffName" varchar(20) DEFAULT NULL COMMENT '服务人员名',
  PRIMARY KEY ("orderItemId"),
  KEY "idx_trade_order_itemorderId" ("orderId") USING BTREE,
  KEY "idx_trade_order_itemgoodsId" ("goodsId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "trade_refund";
CREATE TABLE "trade_refund" (
  "refundId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "orderId" bigint(20) NOT NULL COMMENT '订单id',
  "amount" decimal(10,2) NOT NULL COMMENT '退款金额',
  "reason" varchar(20) DEFAULT NULL COMMENT '退款原因',
  "remark" varchar(20) DEFAULT NULL COMMENT '退款备注',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "status" int(1) NOT NULL COMMENT '状态',
  "refundTime" datetime DEFAULT NULL COMMENT '退款时间',
  "way" int(1) NOT NULL COMMENT '退款方式/渠道',
  PRIMARY KEY ("refundId"),
  KEY "idxtrade_refund_orderId" ("orderId") USING BTREE,
  KEY "idxtrade_refund_systemId" ("systemId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS "trade_withdraw";
CREATE TABLE "trade_withdraw" (
  "withdrawId" bigint(20) NOT NULL,
  "systemId" bigint(20) NOT NULL,
  "memberId" bigint(20) DEFAULT NULL COMMENT '提现会员id',
  "merchantId" bigint(20) DEFAULT NULL COMMENT '提现商户id',
  "accountId" bigint(20) DEFAULT NULL COMMENT '提现账户id',
  "amount" decimal(10,2) DEFAULT NULL COMMENT '金额',
  "way" int(1) NOT NULL COMMENT '提现方式/渠道',
  "creationTime" datetime NOT NULL COMMENT '创建时间',
  "updateTime" datetime NOT NULL COMMENT '更新时间',
  "withdrawAccount" varchar(100) DEFAULT NULL COMMENT '提现目标账户（支付宝userId、微信openid，银行卡号等）',
  "withdrawOrderId" varchar(64) DEFAULT NULL COMMENT '支付宝转账单据号',
  "status" int(1) NOT NULL COMMENT '状态',
  PRIMARY KEY ("withdrawId"),
  KEY "idx_trade_withdrawmemberId" ("memberId") USING BTREE,
  KEY "idx_trade_withdrawsystemId" ("systemId") USING BTREE,
  KEY "idx_trade_withdrawmerchantId" ("merchantId") USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
