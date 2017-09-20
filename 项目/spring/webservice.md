#### webservice 

Web Service主要是为了使原来各孤立的站点之间的信息能够相互通信、共享而提出的一种接口。是一种跨编程语言和跨操作系统平台的**远程调用技术。**. RPC 远程调用

服务端程序采用java编写，客户端程序则可以采用其他编程语言编写 

WebService就是一个应用程序向外界暴露出一个能通过Web进行调用的API，也就是说能用编程的方法通过Web来调用这个应用程序。

-----

Web Service所使用的是Internet上统一、开放的标准，如HTTP、XML、SOAP（简单对象访问协议）、WSDL等 

SOAP(Simple Object Access Protocal):是一个用于分散和分布式环境下网络信息交换的**基于XML的通讯协议** 
SOAP协议 = HTTP协议 + XML数据格式

![webservice是什么](http://blog.csdn.net/wooshn/article/details/8069087/)

WebService采用HTTP协议传输数据，采用XML格式封装数据（请求的数据和结果内容）。


Web Services 拥有三种基本的元素:SOAP、WSDL 以及 UDDI。


> SOAP信封（Envelope）：定义了一个框架，该框架描述了消息中的内容是什么，包括消息的内容、发送者、接收者、处理者以及如何处理这些消息。

> SOAP编码规则：它定义了一种系列化机制，用于交换应用程序所定义的数据类型的实例。

> SOAP RPC表示：它定义了用于表示远程过程调用和应答协定。

> SOAP绑定：它定义了一种使用底层传输协议来完成在节点间交换SOAP信封的约定。

SOAP消息基本上是从发送端到接收端的单向传输，它们常常结合起来执行类似于请求/应答的模式。


SOAP消息

SOAP消息的组成部分

所有的SOAP消息都使用**XML编码**，一条SOAP消息就是一个普通的XML文档，该文档包括下列元素。

> Envelope（信封）元素，必选，可把此XML文档标识为一条SOAP消息。

> Header（报头）元素，可选，包含头部信息（包含了使消息在到达最终目的地之前，能够被路由到一个或多个中间节点的信息）。

> Body(主体)元素，必选，包含所有的调用和响应信息。

> Fault元素，位于Body内，可选，提供有关处理此消息所发生错误的信息。

> Attachment（附件）元素，可选，可通过添加一个或多个附件扩展SOAP消息


SOAP Envelope 

是SOAP消息结构的主要容器，也是SOAP消息的根元素，它必须出现在每个SOAP消息中，用于把此XML文档标示为一条SOAP消息。
encodingStyle属性用于定义在文档中使用的数据类型

SOAP Header元素

应当作为SOAPEnvelope的第一个直接子元素, 它必须使用有效的命名空间。
Header元素用于与消息一起传输附加消息，如身份验证或事务信息。Header元素也可以包含某些属性。SOAP在默认的命名空间中定义了三个属性：actor，mustUnderstand以及encodingStyle。这些被定义在SOAP头部的属性可通知容器如何对SOAP消息进行处理。


SOAP消息的Body块可以包含以下任何元素：

> **RPC方法及其参数**

> 目标应用程序（消息接收者）专用数据

> 报告故障和状态消息的SOAP Fault

所有Body元素的直接子元素都称为Body项，Body项必须由命名空间修饰。

SOAP Fault元素用于在SOAP消息中传输错误及状态信息。如果SOAP消息需要包括SOAP Fault元素，它必须作为一个Body项出现，而且至多出现一次。SOAP Fault包括以下子元素：faultcode,faultstring,faultactor,detail.

SOAP Fault

<faultstringultcode>

供识别故障的代码

<faultstring>

可供人阅读的有关故障的说明

<faultactor>

有关是谁引发故障的信息

<detail>

存留涉及 Body 元素的应用程序专用错误信息










	














