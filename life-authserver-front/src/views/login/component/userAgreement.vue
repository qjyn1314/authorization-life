<template>
  <div class="wrap" v-if="dialogVisible">
    <div class="agreement_model" @click="this.dialogVisible = false"></div>
    <div class="agreement">
      <div class="agreement_view">
        <img
          src="../../../assets/imgs/xx.png"
          alt=""
          class="agreement_view_xx"
          @click="clickDialog"
        />
        <div class="agreement_view_title">服务协议条款</div>
        <div class="agreement_cont">
          <div class="agr_cont_ul">
            <div class="agr_cont_li">
              《支出宝用户注册协议》（以下简称“本协议”）作为支出宝网站（以下简称“本网站”）提供服务的依据，确定个人及企业用户在何种条件、以何种方式使用本网站及本网站的服务（具体载体包括但不限于网页、APP应用程序、微信公众号平台等）。在使用本网站及相关服务前，您应当注册成为本网站用户。进行用户注册时，请您认真阅读本协议，一旦完成注册即表示您已经知悉并了解本协议，接受本协议的条款约束，本协议当即具有合同效力。如您对本协议的任何条款表示异议，您应当选择不使用本网站。本协议包括基于本协议制定的各项规则，所有规则为本协议不可分割的一部分，与本协议具有同等效力。本网站有权随时变更本协议并在本页面发布。当您继续使用本网站及相关服务，则视为您完全接受协议的变更。支出宝网站平台的所有权、运营权、解释权归支出宝（北京）信息技术有限公司所有。
            </div>
            <div class="agr_cont_li agr_cont_li_title">一、服务描述及定义</div>
            <div class="agr_cont_li">
              本网站是连接投标/竞价事件发起方（简称“发起方”）和投标/竞价参与方（简称“参与方”），在支出宝管理的网站（以下简称“本网站”）上举办和管理的在线事件（以下简称“在线事件”）的平台，在线事件包括但不限于投标、竞价、RFI/RFP/RFQ。
              “本网站”“本平台”“支出宝”：指支出宝平台下，载体包括但不限于网页（www.zhichubao.com）、App应用程序、微信公众号等。
            </div>
            <div class="agr_cont_li">
              “企业用户”：依据中华人民共和国法律、法规、部门规章等规范性文件成立并合法存续的企业、机关、社团、事业单位和其他组织。“企业用户”可以根据业务类型在不同的“投标事件”转换“发起方”或“参与方”角色。一个“企业用户”下面可以有多个企业员工作为“个人用户”。
            </div>
            <div class="agr_cont_li">
              “账号”：指用户为使用本网站及相关服务而登记注册的用户身份识别信息。用户只有注册账号才能使用本网站的服务。
            </div>
            <div class="agr_cont_li agr_cont_li_title">二、成为注册企业用户</div>
            <div class="agr_cont_li">（一）用户主体适适格</div>
            <div class="agr_cont_li">
              符合下列条件之一且未曾被本网站封禁账户的自然人或者组织可以注册成为本网站用户，但本网站不保证一定能够通过注册。
            </div>
            <div class="agr_cont_li">
              年满十八周岁，并且具有民事权利能力和民事行为能力的自然人。
            </div>
            <div class="agr_cont_li">
              依据中华人民共和国法律、法规、部门规章等规范性文件成立并合法存续的企业、机关、社团、事业单位和其他组织
            </div>
            <div class="agr_cont_li">（二）企业用户信息完备</div>
            <div class="agr_cont_li">
              企业用户需要提供明确的联系方式、通讯地址、真实名称、个人资质文件等信息，并进行企业认证之后方能使用完整系统功能。为了合法、合规地为用户提供更好的服务，本网站将不时增加或删除用户需提交的信息。如用户不同意使用提供上述信息，则应当停止使用本网站的服务。
            </div>
            <div class="agr_cont_li">（三）平台审核</div>
            <div class="agr_cont_li">
              在企业用户入驻本平台或完成平台认证之前，需经过本平台审核。企业用户的经营内容不得违反法律、行政法规、部门规章、地方性法规等规范性文件，本网站的各项规则和协议以及社会公序良俗，包括但不限于以下情形：
            </div>
            <div class="agr_cont_li">
              1.涉低俗、淫秽、色情、赌博、毒品、迷信、邪教等信息的推广；
            </div>
            <div class="agr_cont_li">2.涉及兼职欺诈的推广；</div>
            <div class="agr_cont_li">3.涉及暴力犯罪的；</div>
            <div class="agr_cont_li">
              4.涉及违反国家民族及宗教政策的本网站有权要求经营者提供相应的资质文件（包括但不限于营业执照、经营许可证等），并依据常识及社会大众通常智力水平做出是否合规的判断而不必须依据公权力机关事后通知、查处、判决等文件。同时，企业用户承诺对所填写或选择的信息承担完全责任，如因企业用户所填写或选择的信息违反本协议之规定或者侵犯任何第三方的合法权益引起的一切纠纷或责任，由企业用户自行承担，本网站不承担任何责任。
            </div>
            <div class="agr_cont_li agr_cont_li_title">三、本网站的使用</div>
            <div class="agr_cont_li">
              企业用户有权依据协议及本网站发布的相关规则，在本站进行“在线事件”的管理工作，具体形式包括但不限于发布竞价，招标（RFI/RFQ/RFP）通知书，邀请“参与方”注册并参与竞价、线上竞价、评标等。企业用户与平台基于服务各自享有必要的权利，承担相应的义务。
            </div>
            <div class="agr_cont_li">(一)企业用户承诺</div>
            <div class="agr_cont_li">
              企业用户承诺使用“支出宝”仅用于企业用户自身业务需要而在此平台上发布“在线事件”或参与“在线事件”。
            </div>
            <div class="agr_cont_li">
              为了使用本网站的服务，企业用户应当提供真实、合法、准确的信息，包括但不限于姓名或名称、个人身份证明、资质证明、通讯地址、职位信息、公司介绍等。企业用户保证如实描述和及时更新，否则承担因信息不真实、不合法、不准确所造成的任何损失。
            </div>
            <div class="agr_cont_li">
              企业用户承诺使用以上内容均出于自愿并已得到有效授权，并承担相应责任。
            </div>
            <div class="agr_cont_li">
              企业用户同意本网站对其注册资料进行审查并要求企业用户对任何问题或疑问做出解释、更正；本网站保留拒绝企业用户注册并且不做出任何解释、说明或通知的权利。
            </div>
            <div class="agr_cont_li">
              企业用户承诺知悉并了解基于本协议制定的各项规则，同意所有规则为本协议不可分割的一部分，与本协议具有同等效力。
            </div>
            <div class="agr_cont_li">
              企业用户应妥善保管自己的账户和密码，该账户在本网站进行的所有操作和活动（包括但不限于信息浏览、发起招标、参与招标等）均视为企业用户主体真实的意思表示。任何因企业用户过错导致的账号安全问题以及由此引起的损失，均由企业用户自行承担。
            </div>
            <div class="agr_cont_li">
              企业用户承诺在使用本网站服务的过程中，严格遵守法律、法规等规范性文件、本网站的各项规则和协议以及社会公序良俗。企业用户违反上述规范产生的任何责任、损失和法律后果，均由企业用户承担。
            </div>
            <div class="agr_cont_li">
              企业用户承诺在使用本网站的服务过程中将最大程度的遵守诚实信用原则。
            </div>
            <div class="agr_cont_li">
              企业用户承诺一旦使用本网站的服务即视为同意接受来自本网站的信息、资讯，包括但不限于寻源信息、活动信息、推广信息等。
            </div>
            <div class="agr_cont_li">
              本网站将不时地指定本网站（所有者）的关联公司或者第三方作为本网站的代理、服务商、技术支持方等，为用户提供本平台相应的服务。企业用户使用相关服务即同意上述关联公司或第三方公司就提供相关服务事项享有与本网站同等权利，承担相应义务。
            </div>
            <div class="agr_cont_li">(二)企业用户行为限制</div>
            <div class="agr_cont_li">
              企业用户的行为不得违反法律、行政法规、部门规章、地方性法规等规范性文件，本网站的各项规则和协议以及社会公序良俗。本网站有权依据常识及社会大众通常智力水平做出是否合规的判断而不必须依据公权力机关事后通知、查处、判决等文件。
            </div>
            <div class="agr_cont_li">
              企业用户不得破坏网站运营秩序，不得从事与本网站服务无关的行为，不得发布除寻源信息、竞价邀请、采购问答等采购及成本管理领域所必需专业信息外的其他内容。
            </div>
            <div class="agr_cont_li">
              企业用户不得以虚假信息注册账号，不得冒用、盗用、借用他人、关联机构或社会名人的名义注册账号名称。
            </div>
            <div class="agr_cont_li">
              企业用户在未经本网站同意的情况下，不得转让或授权他人使用其所有的本网站账号。
            </div>
            <div class="agr_cont_li">
              企业用户不得使用任何机器人软件、脚本等其他方式，自动访问或登录本网站。
            </div>
            <div class="agr_cont_li">
              企业用户不得以任何技术手段恶意破坏网站或以大量发布垃圾信息等方式干扰网站运营。
            </div>
            <div class="agr_cont_li">
              企业用户不得以任何方式对本网站（以及已有或可能发布的相关App、微信公众号等平台服务载体）进行反向工程，且在使用过程中需遵守本网站有关数据爬虫、抓取等行为的限制规则
            </div>
            <div class="agr_cont_li">
              企业用户不得在未经本网站和“发起方”或“参与方”并在相对方的同意下，将本网站的任何信息、资料以及在招标找工作过程中任何信息、资料用作其他商业用途、出售或者传播（包括但不限于以复制、修改、转发、收集、翻译等形式制作复制品、衍生品，或使其公布于众）。
            </div>
            <div class="agr_cont_li">(三)网站使用信息的收集及使用</div>
            <div class="agr_cont_li">
              为了更好的为企业用户提供服务，以及可能用于本网站的推广，本网站有权收集和使用企业用户在使用本网站服务的过程中产生的数据、使用痕迹等信息（非个人信息），包括但不限于寻源信息、招标数量、企业用户数量、参与招标行业分布、企业活跃度等。本网站不会将任何个人信息以出售、交换等方式提供给任何与本网站服务无关的第三方。
            </div>
            <div class="agr_cont_li">(四)投标活动及行为</div>
            <div class="agr_cont_li">
              4.
              1投标。如果参与方受邀请参与在线事件，作为“参与方”，则“发起方”将保留更正、变动或撤销该在线事件的权利。“发起方”将保留接受或拒绝“参与方”的全部或部分标书的权利。递标不构成契约，也不代表有望与参与者建立业务关系，递标只表示参与方提出了一个实盘，发起方可能根据下面的第4.2款接受该实盘，从而形成一个契约，发起方不负责承担参与方因投标而发生的准备、介绍或其他任何方面的费用。
            </div>
            <div class="agr_cont_li">
              4.2报价。除非发起方允许无约束力竞标，否则参与方通过在线时间提交的投标都被视为具有法律效力且无限定条件（数据输入错误者可与发起方协商，申请作废）。
            </div>
            <div class="agr_cont_li">
              4.3程序和规则。参与方还须统一接受本网站和发起方所制定的程序和规则的约束。
            </div>
            <div class="agr_cont_li">
              4.4保密。对于所有企业用户和密码、在线事件内容、本网站和/或发起方提供的其他机密资料，以及参与方或其他参与者组织者提供的所有投标，参与者都必须予以保密，不得将其泄露给任何第三方。
            </div>
            <div class="agr_cont_li">
              4.5仅限通过本网站投标。除非发起方有明确的要求，否则参与方只能通过本网站提供的在线投标系统进行投标，不得通过任何其他系统投标，包括但不限于邮寄、快递、传真、电子邮件或口头投标。
            </div>
            <div class="agr_cont_li">
              4.6道德规范。“发起方”与“参与方”都不得有违反道德规范的行为，如有发现违反在线事件公司运营的行为，请与相关项目小组联系并通知本网站。如果参与方在参与在线事件期间遇到任何问题，应立即通知本网站。
            </div>
            <div class="agr_cont_li">
              4.7免责条款：支出宝对于“发起方”运营者所提出的竞价信息的真实性、可执行性、最终是否与其他参与方达成合同等商务约定不做出承诺，不承担任何联带连带责任。
            </div>
            <div class="agr_cont_li">
              4.8企业用户发布的找“在线事件”相关信息均应遵守法律、行政法规、部门规章等规范性文件，本网站的各项规则和协议以及社会公序良俗，并确保不侵犯任何第三人的合法权益（包括但不限于知识产权、商业秘密等）。
            </div>
            <div class="agr_cont_li">
              4.9本网站不对“在线事件”的内容做实质性审查，不保证在线事件的真实、完整、准确。
            </div>
            <div class="agr_cont_li">
              4.10有效期：在线事件结束后，此“用户注册服务协议”中的“投标活动及行为”条款和条件仍然有效。
            </div>
            <div class="agr_cont_li">(五)用户反馈</div>
            <div class="agr_cont_li">
              企业用户在使用本网站服务的过程中，可通过本网站提供的反馈渠道对本网站及本网站的服务提出任何形式的评价、建议或其他意见。
            </div>
            <div class="agr_cont_li">
              企业用户在在线事件活动完成之后，可以在本网站规定的位置以合适的形式发表对另一方用户的评价、建议或其他意见，包括但不限于招标流程、成功案例等。
            </div>
            <div class="agr_cont_li">
              企业用户的上述反馈应当真实、客观，不得违反法律法规等规范性文件、本网站的各项规则和协议以及社会公序良俗，否则本网站有权进行相应处理。
            </div>
            <div class="agr_cont_li">(六)推广和宣传</div>
            <div class="agr_cont_li">
              除企业用户特别要求且本网站接受该要求外，本网站有权在不做出任何通知的情况下，在本网站页面或者与本网站有关的市场宣传、推广材料上公开使用用户的公开信息，包括但不限于名称、商号、商标、标语等。本网站承诺上述信息不用于与本网站服务无关的事项上。
            </div>
            <div class="agr_cont_li agr_cont_li_title">四、服务的中断或终止</div>
            <div class="agr_cont_li">
              企业用户在使用本网站服务的所有行为，产生的所有信息，本网站有权但无义务依据法律法规、社会公序良俗、本网站的规则和协议，做出审查。本网站有权中断或终止对违规用户的服务。该企业用户以其他名义再次注册账户的，本网站有权再次中断或终止对该用户的服务。
            </div>
            <div class="agr_cont_li">
              企业用户长时间未登录账户超过6个月的，本网站有权中断或终止对该企业用户的服务。
            </div>
            <div class="agr_cont_li">企业用户申请终止服务的，经本网站同意后可以解除该用户协议</div>
            <div class="agr_cont_li">
              本网站中断或终止对企业用户提供服务后，有权但并无义务继续保留企业用户在本网站的全部资料和信息（包括但不限于企业用户的账号信息、交易信息等）。
            </div>
            <div class="agr_cont_li">
              中断或终止对企业用户的服务并不免除用户因违反法律法规、社会公序良俗、本网站的规则和协议，而对本网站、网站其他用户或第三方应负的损失赔偿责任。
            </div>
            <div class="agr_cont_li agr_cont_li_title">五、责任声明</div>
            <div class="agr_cont_li">
              本网站作为信息服务与交流平台，所有寻源信息、商务信息、社区交流信息均由企业用户自行上传、发布。本网站对企业用户的真实资信状况、企业用户发布信息的真实性、合法性、准确性等不作保证。因上述原因导致的对其他用户或第三方造成的损失，本网站不承担责任。
              本网站致力于为用户提供优质、稳定的服务。但本网站不保证本网站系统：能够满足所有用户需求；不出现任何的系统宕机、波动；不出现任何的数据丢失。因网络运营商原因、本网站进行网络调整、正常的系统维护升级等造成的本网站无法正常访问，本网站不承担任何责任
            </div>
            <div class="agr_cont_li">
              企业用户通过本网站获取的任何资料和信息，无论其来自于本网站或任何外部链接，本网站对其不做出任何保证。本网站提供与其它互联网上的网站或资源的链接，企业用户可能会因此跳转至其它运营商经营的网站，但不表示本网站与这些运营商有任何关系。其它运营商经营的网站均由各经营者自行负责，不属于本网站控制及负责范围之内。对于存在或来源于此类网站或资源的任何内容、广告、物品或其它资料，本网站亦不予保证或负责。因使用或依赖任何此类网站或资源发布的或经由此类网站或资源获得的任何内容、物品或服务所产生的任何损害或损失，本网站不负任何直接或间接的责任。涉及上述条款中的情形，而造成的用户利润、商誉等损失，本网站不承担任何直接、间接、附带、惩罚性的赔偿。
            </div>
            <div class="agr_cont_li agr_cont_li_title">六、陈述与保证</div>
            <div class="agr_cont_li">（一）知识产权</div>
            <div class="agr_cont_li">
              本协议条款明确规定的，以及任何与本网站服务有关的内容的知识产权，由本网站或相关权利人所享有。该条款所指内容包括但不限于本网站设计、本网站代码、本网站内容、本网站资讯、用户信息等。该条款所指知识产权包括但不限于专利权、著作权、商标权等。
            </div>
            <div class="agr_cont_li">
              企业用户应确保本网站的完整性，不得擅自修改本网站，不得反向编译本网站
            </div>
            <div class="agr_cont_li">（二）隐私权</div>
            <div class="agr_cont_li">
              本网站承诺不将企业用户及相关个人信息用于非本网站服务的用途。
            </div>
            <div class="agr_cont_li">
              本网站不向任何第三方提供企业用户个人信息，无论出于营利或非营利的目的。但属于为企业用户提供服务所必需，委托第三方为企业用户提供服务或经过用户同意的除外。
            </div>
            <div class="agr_cont_li">
              因第三方提出的涉及知识产权的权利主张或者行政、司法机关的介入，本网站有权披露用户的个人信息。
            </div>
            <div class="agr_cont_li">
              用户使用本网站所产生的不含个人特征的信息，本网站有权收集和利用，详见本协议“用户信息的收集与使用”部分。
            </div>
            <div class="agr_cont_li">（三）保密义务</div>
            <div class="agr_cont_li">
              企业用户保证在使用本网站过程中所获悉的属于本网站及他方的且无法自公开渠道获得的文件及资料（包括但不限于个人信息、商业秘密、公司计划、运营活动、财务信息、技术信息、经营信息及其他商业秘密）属于保密信息。对于此等信息，本网站对企业用户负有保密义务，企业用户之间也负有保密义务。未经该资料和文件的原提供方同意，任何一方不得向第三方泄露该保密信息的全部或者部分内容。但法律、法规、行政规章另有规定或者双方另有约定的除外。
            </div>
            <div class="agr_cont_li agr_cont_li_title">七、协议的更新及修改</div>
            <div class="agr_cont_li">
              本网站保留随时修正、更新本协议的权利。一旦发生相关修订或更新，本网站会将修订和更新的内容及时在本页面发布，用户如认为变更无法接受，应该停止使用本网站相关服务。如果继续使用本网站相关服务的，则视为用户接受变更条款并愿意受其约束。
            </div>
            <div class="agr_cont_li agr_cont_li_title">八、协议解除、终止</div>
            <div class="agr_cont_li">有以下情形的，本协议归于解除或终止：</div>
            <div class="agr_cont_li">
              本网站基于法律法规等规范性文件、本网站的各项规则和协议以及社会公序良俗，终止对用户提供服务的。该情形的协议终止并不免除用户应承担的相应责任。
            </div>
            <div class="agr_cont_li">
              一方主体归于消灭、撤销等无法有效存续状态的。依据法律法规的规定处理未决问题。
            </div>
            <div class="agr_cont_li">
              因不可抗力使本协议无法继续履行或者没有履行必要和意义。不可抗力是指不能遇见、不能避免并不能克服的客观情况，包括但不限于战争、台风、水灾、火灾、雷击或地震、罢工、暴动、法定疾病、黑客攻击、网络病毒、电信部门技术管制、政府行为或任何其它自然或人为造成的灾难等客观情况。
            </div>
            <div class="agr_cont_li">
              本协议终止或更新时，企业用户不接受新的协议。企业用户应当立即停止使用本网站的服务。
            </div>
            <div class="agr_cont_li agr_cont_li_title">九、违约责任</div>
            <div class="agr_cont_li">
              任何一方违反本协议约定的行为均构成违约行为，均应承担相应的责任。
            </div>
            <div class="agr_cont_li">
              企业用户未按照本协议约定使用本网站的服务，本网站有权对企业用户停止服务或对企业用户发布的信息进行删除。
            </div>
            <div class="agr_cont_li">
              对于企业用户的任何违反本协议规定的行为，本网站有权采取相应处理措施，方式包括但不限于记录不良行为、调整用户信用等级、黑白名单限制、封禁用户账号等，形式包括系统后台记录或网站公示公开。该等处理措施将影响企业用户使用本网站的服务，涉及违法、犯罪的，本网站将移交司法机关处理。
            </div>
            <div class="agr_cont_li">
              遭受不可抗力事件的一方可暂行中止履行本合同项下的义务直至不可抗力事件的影响消除为止，并且无需为此而承担违约责任；但应尽最大努力克服该事件，减轻其负面影响。
            </div>
            <div class="agr_cont_li agr_cont_li_title">十、其他</div>
            <div class="agr_cont_li">支出宝拥有对本协议的最终解释权。</div>
            <div class="agr_cont_li">
              企业用户与本平台的任何纠纷，可以通过协商的途径解决。协商不成的，任何一方可向本网站登记住所地的人民法院提起诉讼。
            </div>
            <div class="agr_cont_li">
              本协议的订立、生效、解释、执行、管辖、争议的解决均适用中华人民共和国法律。
            </div>
            <div class="agr_cont_li agr_cont_li_title">支出宝（北京）信息技术有限公司</div>
          </div>
        </div>
        <div class="agreement_btn_all">
          <div class="agreement_btn_l" @click="clickDialog">不同意</div>
          <div class="agreement_btn_r" @click="checkedClick">已阅读并同意此条款</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'userAgreement',
  data () {
    return {
      dialogVisible: false
    }
  },
  created () {
  },
  methods: {
    showDialog () {
      this.dialogVisible = true
    },
    clickDialog () {
      this.dialogVisible = false
      this.$emit('checkedClick', [])
    },
    checkedClick () {
      this.dialogVisible = false
      this.$emit('checkedClick', ['1'])
    }
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  .agreement_model {
    position: fixed;
    left: 0;
    top: 0;
    background: rgba(0, 0, 0, 0.3);
    width: 100%;
    height: 100%;
    z-index: 0;
  }
  .agreement {
    width: 40%;
    min-height: 55vh;
    background: #ffffff;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: fixed;
    .agreement_view {
      width: 94.5%;
      position: relative;
      text-align: center;
      .agreement_view_xx {
        position: absolute;
        right: 0;
        top: 26px;
        width: 17px;
        height: 17px;
        background: #cccccc;
        cursor: pointer;
      }
      .agreement_view_title {
        width: 100%;
        text-align: center;
        font-size: 18px;
        line-height: 28px;
        font-weight: 800;
        color: #333333;
        margin: 20px 0 20px 0;
      }
      .agreement_cont {
        width: 100%;
        height: 400px;
        background: #ffffff;
        border: 1px solid #ebebeb;
        overflow: auto;
        overflow-y: scroll;
        .agr_cont_ul {
          width: 96%;
          margin-left: 16px;
          margin-top: 16px;
          .agr_cont_li {
            font-size: 14px;
            font-weight: 400;
            color: #333333;
            line-height: 22px;
            margin-bottom: 14px;
            text-indent: 24px;
            text-align: left;
          }
          .agr_cont_li_title {
            font-size: 16px;
            font-weight: 600;
            text-indent: 0px;
          }
        }
      }
      /*滚动条样式*/
      .agreement_cont::-webkit-scrollbar {
        /*滚动条整体样式*/
        width: 12px; /*高宽分别对应横竖滚动条的尺寸*/
        height: 5px;
        cursor: pointer;
      }
      .agreement_cont::-webkit-scrollbar-thumb {
        /*滚动条里面小方块*/
        border-radius: 7px;
        -webkit-box-shadow: inset 0 0 5px rgba(66, 36, 36, 0.2);
        background: rgba(0, 0, 0, 0.1);
        cursor: pointer;
      }
      .agreement_cont::-webkit-scrollbar-track {
        /*滚动条里面轨道*/
        /* -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); */
        background-color: transparent;
        border-radius: 0;
        /* background: rgba(0, 0, 0, 0.1); */
      }
      .agreement_btn_all {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 16px 0 24px 0;

        .agreement_btn_l {
          width: 118px;
          height: 38px;
          display: inline-block;
          line-height: 40px;
          text-align: center;
          background: #FFFFFF;
          border-radius: 2px;
          border: 1px solid #D6D6D6;
          font-size: 16px;
          font-weight: 500;
          color: #333333;
          cursor: pointer;
        }
        .agreement_btn_r {
          margin-left: 16px;
          width: 200px;
          height: 40px;
          display: inline-block;
          line-height: 40px;
          text-align: center;
          background: #1ba1ba;
          border-radius: 2px;
          font-size: 16px;
          font-weight: 500;
          color: #ffffff;
          cursor: pointer;
        }
      }
    }
  }
}
</style>
