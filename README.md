# 插件名称

FishClear 高效快捷便利的清理插件

## 特点

- 支持生物自定义清理 可根据需求清理生物
- 自定义掉落物清理 支持分世界清理
- 支持清理回收 清理的掉落物可以直接进入回收站
- 高自定义 阻止玩家丢弃
- 支持自定义指令以及提示文本
- ...

## 安装

1. 下载 FishClear 的最新版本 **[下载链接](https://motci.cn/view/SoBadFish/job/FishCleaner/)**。
2. 将插件文件放入服务器的 `plugins` 文件夹。
3. 重启服务器。

## 使用方法

### 命令

- `/fcl`: 管理员执行清理指令
- `/fcl open <page>`: 开启垃圾站
- `/fcl clean <page>/all`: 清理垃圾站
- `/fcl drop`: 开启/关闭 丢弃物品

## 配置文件

```yaml
######################################
#
#     清理者
#
#####################################
# 插件所有提示的前缀
plugin-title: "&7[ &a清道夫 &7]"

plugin-cmd: "fcl"

# 清理设置选项
# time 清理间隔 单位(s)
# clearWorld 清理的世界 ALL为全部
# 如果只是单独的世界请写入世界的名称，使用;分割多个世界
# 示例: 地图1;地图2
clear-settings:
  # 清理的时间间隔
  time: 300
  # 清理的地图
  clear-world: "ALL"
  # 清理生物配置
  clear-entity:
    # 是否清理生物
    enable: false
    # 清理的地图
    clear-world: "ALL"
    # 生物过滤器 部分生物将不纳入清理范围
    # animal代表全部友好生物
    # mob代表全部怪物
    # 如果有其他生物不想清理 加入名称或者显示的名字或者id
    filter:
      - "animal"

  # 清理间隔提示
  intervalTime: [60,30]
  # 将清理的物品增加到垃圾桶的世界
  add-trash-world: "ALL"

# 垃圾桶的配置
trash-settings:
  # 是否启用
  enable: true
  # 垃圾桶的页数
  page-size: 15

  # 过滤id 以下id的物品不进入垃圾桶
  filter:
    # NBT物品不在忽略id列表
    ignoreNbt: true
    # id列表
    ids: []


other-setting:
  # 控制玩家丢弃物品
  # null代表不控制
  # cmd 代表玩家可以通过指令自由开关是否丢弃
  # force 代表玩家不可丢弃
  # 在变量后面增加:则划分世界 不加默认全部世界用
  # 使用& 区分不同的配置选项
  control-drop: "null"
  # 丢弃物品是否直接进入垃圾桶
  add-trash: false
  # 掉落物品定时配置
  item-timing-setting:
    # 是否启用
    enable: true
    # 时间 单位秒
    time: 300



# 内容文本提示的配置
msg-settings:
  # 显示的位置 支持 tip,popup,action,msg
  display-type: "tip"
  # 避免控制台刷屏 只发送给在当前世界的玩家
  only-display-player: true
  # 信息提示
  message:
    running-msg: "&e扫地大妈将在 ${time} 秒后清理 ${world-name} 地上的掉落物"

    clear-success-msg: "&a扫地大妈已将 ${world-name} 打扫干净 共计 ${count-item}"

    drop-cancel: "&c${world-name} 不允许丢弃物品 ${control}"

    drop-open: "&7你已开启在 ${world-name} 丢掉物品"

    drop-close: "&7你已关闭在 ${world-name} 丢掉物品"
  # 变量文本
  variable:
    # x代表数量不要将这个删除
    count-item: "x 个物品"
    # x代表数量不要将这个删除
    count-entity: "x 个生物"
    trash-title: "&b垃圾桶 第 ${page} 页"
    # 当玩家可以用指令开关时提示此文本
    control: "可通过指令 ${control-cmd} 关闭"

...

