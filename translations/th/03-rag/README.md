# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

ชมเซสชันสดนี้ซึ่งอธิบายวิธีเริ่มต้นกับโมดูลนี้:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

ในโมดูลก่อนหน้านี้ คุณได้เรียนรู้วิธีการสนทนากับ AI และจัดโครงสร้าง prompt อย่างมีประสิทธิภาพ แต่สิ่งหนึ่งที่จำกัดโดยพื้นฐานคือ: โมเดลภาษาเรียนรู้ได้เฉพาะสิ่งที่ได้รับการฝึกฝนไว้เท่านั้น มันไม่สามารถตอบคำถามเกี่ยวกับนโยบายบริษัทของคุณ เอกสารโครงการของคุณ หรือข้อมูลอื่นใดที่ไม่ได้ฝึกสอนมา

RAG (Retrieval-Augmented Generation) แก้ปัญหานี้ แทนที่จะพยายามสอนโมเดลด้วยข้อมูลของคุณ (ซึ่งมีค่าใช้จ่ายสูงและไม่สะดวก) คุณให้มันสามารถค้นหาข้อมูลในเอกสารของคุณได้ เมื่อมีคนถามคำถาม ระบบจะค้นหาข้อมูลที่เกี่ยวข้องและรวมข้อมูลนั้นเข้าไปใน prompt แล้วโมเดลจะตอบโดยอิงจากบริบทที่ดึงมาได้

คิดว่า RAG คือการสร้างห้องสมุดอ้างอิงให้โมเดล เมื่อคุณถามคำถาม ระบบจะทำตามขั้นตอนนี้:

1. **User Query** - คุณถามคำถาม
2. **Embedding** - แปลงคำถามเป็นเวกเตอร์
3. **Vector Search** - ค้นหาชิ้นส่วนเอกสารที่มีความคล้ายคลึง
4. **Context Assembly** - เพิ่มชิ้นส่วนที่เกี่ยวข้องไปใน prompt
5. **Response** - LLM สร้างคำตอบโดยอิงจากบริบทนั้น

วิธีนี้ช่วยให้คำตอบของโมเดลยึดติดกับข้อมูลจริงของคุณ แทนที่จะพึ่งพาความรู้จากการฝึกฝนหรือการสร้างคำตอบขึ้นมาเอง

## Prerequisites

- ผ่าน [Module 00 - Quick Start](../00-quick-start/README.md) (สำหรับตัวอย่าง Easy RAG ที่อ้างอิงในโมดูลนี้)
- ผ่าน [Module 01 - Introduction](../01-introduction/README.md) (ได้ติดตั้งทรัพยากร Azure OpenAI แล้ว รวมถึงโมเดล embedding `text-embedding-3-small`)
- มีไฟล์ `.env` ในไดเรกทอรีราก พร้อมข้อมูลรับรอง Azure (สร้างโดยคำสั่ง `azd up` ใน Module 01)

> **Note:** หากยังไม่ได้ทำ Module 01 ให้ทำตามคำแนะนำการติดตั้งที่นั่นก่อน คำสั่ง `azd up` จะติดตั้งทั้งโมเดลแชท GPT และโมเดล embedding ที่ใช้ในโมดูลนี้

## Understanding RAG

แผนภาพด้านล่างแสดงแนวคิดหลัก: แทนที่จะพึ่งพาข้อมูลฝึกฝนของโมเดลเพียงอย่างเดียว RAG ให้อ้างอิงห้องสมุดจากเอกสารของคุณเพื่อให้โมเดลตรวจสอบก่อนสร้างคำตอบแต่ละครั้ง

<img src="../../../translated_images/th/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*แผนภาพนี้แสดงความแตกต่างระหว่าง LLM ปกติ (ที่เดาจากข้อมูลฝึกฝน) กับ LLM ที่เสริมด้วย RAG (ที่ตรวจสอบเอกสารของคุณก่อน)*

นี่คือการเชื่อมต่อของแต่ละส่วนแบบครบวงจร ขั้นตอนการทำงานของคำถามผู้ใช้ไล่จากการ embedding, ค้นหาเวกเตอร์, การประกอบบริบท และการสร้างคำตอบ — แต่ละขั้นสร้างต่อจากขั้นก่อนหน้า:

<img src="../../../translated_images/th/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*แผนภาพนี้แสดงไลน์การทำงาน RAG แบบครบวงจร — คำถามผู้ใช้ไหลผ่าน embedding, ค้นหาเวกเตอร์, ประกอบบริบท และสร้างคำตอบ*

ส่วนที่เหลือของโมดูลนี้จะเดินผ่านแต่ละขั้นตอนโดยละเอียด พร้อมโค้ดที่จะรันและแก้ไขได้

### Which RAG Approach Does This Tutorial Use?

LangChain4j มีสามวิธีในการใช้ RAG โดยแต่ละวิธีมีระดับนามธรรมต่างกัน แผนภาพด้านล่างเปรียบเทียบแต่ละวิธี:

<img src="../../../translated_images/th/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*แผนภาพนี้เปรียบเทียบสามวิธี RAG ของ LangChain4j — Easy, Native และ Advanced — แสดงส่วนประกอบหลักและเวลาที่ควรใช้แต่ละวิธี*

| Approach | What It Does | Trade-off |
|---|---|---|
| **Easy RAG** | เชื่อมต่อทุกอย่างอัตโนมัติผ่าน `AiServices` และ `ContentRetriever` คุณเพียงแค่ใส่ annotation ให้กับ interface, แนบ retriever แล้ว LangChain4j จะจัดการ embedding, ค้นหา และประกอบ prompt ให้เอง | โค้ดน้อย แต่คุณจะไม่เห็นรายละเอียดของแต่ละขั้นตอน |
| **Native RAG** | คุณเรียกใช้โมเดล embedding, ค้นหาใน store, สร้าง prompt และสร้างคำตอบทีละขั้นตอนอย่างชัดเจน | โค้ดมากขึ้น แต่คุณเห็นและแก้ไขได้ในทุกขั้นตอน |
| **Advanced RAG** | ใช้ `RetrievalAugmentor` framework ที่รองรับตัวแปลงคำถาม, ตัวจัดเส้นทาง, ตัวจัดอันดับใหม่ และตัวฉีดเนื้อหา สำหรับงานสเกลโปรดักชัน | ยืดหยุ่นสูงสุด แต่มีความซับซ้อนมาก |

**บทแนะนำนี้ใช้วิธี Native** ทุกขั้นตอนของไลน์การทำงาน RAG — การ embedding คำถาม, ค้นหาในเวกเตอร์สโตร์, การประกอบบริบท, และการสร้างคำตอบ — เขียนโดยละเอียดใน [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ซึ่งทำให้คุณเห็นและเข้าใจแต่ละขั้นตอนอย่างชัดเจน เมื่อคุณเข้าใจแนวทางนี้แล้ว คุณอาจข้ามไป Easy RAG เพื่อสร้างต้นแบบอย่างรวดเร็ว หรือ Advanced RAG สำหรับระบบโปรดักชัน

> **💡 เคยเห็น Easy RAG แล้ว?** โมดูล [Quick Start](../00-quick-start/README.md) มีตัวอย่าง Document Q&A ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) ที่ใช้ Easy RAG โดย LangChain4j จัดการ embedding, ค้นหา และประกอบ prompt อัตโนมัติ โมดูลนี้จะเปิดเผยขั้นตอนเหล่านั้นให้เห็นและควบคุมได้ด้วยตนเอง

แผนภาพด้านล่างแสดงไลน์การทำงาน Easy RAG จากตัวอย่าง Quick Start สังเกตว่า `AiServices` และ `EmbeddingStoreContentRetriever` ซ่อนความซับซ้อน คุณเพียงโหลดเอกสาร แนบ retriever แล้วรับคำตอบ วิธีย่อย Native ในโมดูลนี้จะเปิดเผยแต่ละขั้นตอน:

<img src="../../../translated_images/th/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*แผนภาพนี้แสดงไลน์ Easy RAG จาก `SimpleReaderDemo.java` เปรียบเทียบกับวิธี Native ในโมดูลนี้: Easy RAG ซ่อน embedding, การดึงข้อมูล และการประกอบ prompt ไว้ที่ `AiServices` และ `ContentRetriever` — คุณโหลดเอกสาร แนบ retriever แล้วได้คำตอบ Native เปิดเผยขั้นตอนนี้ให้คุณเรียกใช้ทุกขั้นตอน (embedding, ค้นหา, ประกอบบริบท, สร้างคำตอบ) ด้วยตัวเอง ทำให้เห็นและควบคุมได้เต็มที่*

## How It Works

ไลน์การทำงาน RAG ในโมดูลนี้แบ่งเป็นสี่ขั้นตอนที่ทำเป็นลำดับ เมื่อผู้ใช้ถามคำถาม ขั้นแรกเอกสารที่อัปโหลดจะถูก **แยกและแบ่งเป็นชิ้นเล็ก** เพื่อจัดการได้ง่าย ชิ้นเหล่านี้ถูกแปลงเป็น **embedding แบบเวกเตอร์** และจัดเก็บเพื่อเปรียบเทียบทางคณิตศาสตร์ เมื่อมีคำถามระบบจะทำ **การค้นหาเชิงความหมาย** เพื่อหาชิ้นส่วนที่เกี่ยวข้องมากที่สุด แล้วส่งต่อเป็นบริบทให้ LLM ทำ **การสร้างคำตอบ** ส่วนถัดไปจะอธิบายแต่ละขั้นตอนพร้อมโค้ดและแผนภาพ มาดูขั้นตอนแรกกัน

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

เมื่อคุณอัปโหลดเอกสาร ระบบจะทำการแยกวิเคราะห์เอกสาร (PDF หรือข้อความธรรมดา) แนบเมตาดาต้าอย่างชื่อไฟล์ แล้วแบ่งเป็นชิ้นเล็ก — ส่วนที่เล็กลงซึ่งพอดีกับหน้าต่างบริบทของโมเดล ชิ้นเหล่านี้ทับซ้อนกันเล็กน้อยเพื่อไม่ให้สูญเสียบริบทตรงขอบเขต

```java
// แยกวิเคราะห์ไฟล์ที่อัปโหลดและห่อหุ้มในเอกสาร LangChain4j
Document document = Document.from(content, metadata);

// แบ่งเป็นชิ้นส่วนขนาด 300 โทเค็นโดยทับซ้อนกัน 30 โทเค็น
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
แผนภาพด้านล่างแสดงการทำงานเป็นภาพ สังเกตว่าชิ้นแต่ละชิ้นแบ่งทับซ้อนบางส่วนกับชิ้นข้างเคียง — การทับซ้อน 30 โทเคนช่วยให้บริบทสำคัญไม่ขาดช่วงตรงขอบเขต:

<img src="../../../translated_images/th/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*แผนภาพนี้แสดงเอกสารถูกแบ่งเป็นชิ้นละ 300 โทเคน ที่มีการทับซ้อน 30 โทเคน เพื่อรักษาบริบทตรงขอบเขต*

> **🤖 ลองใช้ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิด [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) แล้วถาม:
> - "LangChain4j แบ่งเอกสารเป็นชิ้นเล็กๆ อย่างไรและทำไมการทับซ้อนถึงสำคัญ?"
> - "ขนาดชิ้นที่เหมาะสมสำหรับเอกสารแต่ละประเภทคือเท่าไหร่และเพราะเหตุใด?"
> - "จัดการกับเอกสารหลายภาษา หรือมีการจัดรูปแบบพิเศษอย่างไร?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

แต่ละชิ้นจะถูกแปลงเป็นตัวแทนเชิงตัวเลขที่เรียกว่า embedding — โดยเทียบเท่ากับเครื่องมือแปลงความหมายเป็นตัวเลข โมเดล embedding ไม่ใช่โมเดลแชทที่ "ฉลาด" ที่สามารถทำตามคำสั่ง ตรรกะ หรือให้คำตอบได้ สิ่งที่มันทำได้คือแม็ปข้อความลงในพื้นที่ทางคณิตศาสตร์ที่ความหมายใกล้เคียงกันจะอยู่ติดกัน — เช่น "car" ใกล้กับ "automobile," "refund policy" ใกล้กับ "return my money" คิดว่าโมเดลแชทเปรียบเหมือนคนที่คุณคุยด้วย ส่วนโมเดล embedding คือระบบจัดเก็บข้อมูลที่ดีเยี่ยม

แผนภาพด้านล่างแสดงภาพแนวคิดนี้ — ข้อความเข้ามา เปลี่ยนเป็นเวกเตอร์ตัวเลข และความหมายใกล้เคียงกันจะอยู่จุดใกล้กัน:

<img src="../../../translated_images/th/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*แผนภาพนี้แสดงวิธีที่โมเดล embedding แปลงข้อความเป็นเวกเตอร์ตัวเลข โดยวางความหมายใกล้เคียง เช่น "car" และ "automobile" ให้เข้าใกล้กันในพื้นที่เวกเตอร์*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```
  
แผนภาพคลาสนี้แสดงสองไลน์การไหลใน pipeline RAG และคลาสของ LangChain4j ที่ใช้งาน ไลน์ **การ ingest** (รันครั้งเดียวตอนอัปโหลด) ทำการแบ่งเอกสาร embed ชิ้น และจัดเก็บผ่าน `.addAll()` ขณะที่ไลน์ **การ query** (รันทุกครั้งเมื่อมีคำถาม) embed คำถาม ค้นหาใน store ผ่าน `.search()` และส่งบริบทที่ตรงกันให้โมเดลแชท ทั้งสองไลน์รวมกันที่ interface ร่วม `EmbeddingStore<TextSegment>`:

<img src="../../../translated_images/th/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*แผนภาพนี้แสดงสองไลน์การไหลใน pipeline RAG — ingest และ query — และการเชื่อมโยงผ่าน EmbeddingStore ร่วม*

หลังจากเก็บ embedding แล้ว เนื้อหาที่คล้ายกันจะรวมกลุ่มกันตามธรรมชาติในพื้นที่เวกเตอร์ ภาพด้านล่างแสดงว่าเอกสารหัวข้อใกล้เคียงกันจะรวมเป็นกลุ่มใกล้เคียงกัน ซึ่งทำให้การค้นหาเชิงความหมายเป็นไปได้:

<img src="../../../translated_images/th/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*ภาพนี้แสดงเอกสารที่เกี่ยวข้องรวมกลุ่มกันในพื้นที่เวกเตอร์ 3 มิติ โดยมีหัวข้ออย่าง Technical Docs, Business Rules และ FAQs แยกเป็นกลุ่มชัดเจน*

เมื่อผู้ใช้ค้นหา ระบบจะทำสี่ขั้นตอน: embed เอกสารครั้งเดียว, embed คำถามทุกครั้งที่ค้นหา, เปรียบเทียบเวกเตอร์คำถามกับเวกเตอร์ทั้งหมดโดยใช้ cosine similarity และคืนชิ้นที่คะแนนสูงสุดจำนวนบนสุด แผนภาพด้านล่างแสดงแต่ละขั้นตอนและคลาสใน LangChain4j ที่เกี่ยวข้อง:

<img src="../../../translated_images/th/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*แผนภาพนี้แสดงขั้นตอนสี่ขั้นตอนของการค้นหา embedding: embed เอกสาร, embed คำถาม, เปรียบเทียบเวกเตอร์โดยใช้ cosine similarity และคืนผลลัพธ์ที่ดีที่สุด*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

เมื่อคุณถามคำถาม คำถามของคุณก็จะถูกแปลงเป็น embedding ด้วยเช่นกัน ระบบเทียบ embedding ของคำถามกับ embedding ของชิ้นเอกสารทั้งหมด เพื่อค้นหาชิ้นที่มีความหมายใกล้เคียงที่สุด — ไม่ใช่แค่คำหลักตรงกัน แต่เป็นความหมายเชิงความสัมพันธ์จริงๆ

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```
  
แผนภาพด้านล่างเปรียบเทียบการค้นหาเชิงความหมายกับการค้นหาด้วยคำหลัก การค้นหาด้วยคำหลัก "vehicle" อาจพลาดชิ้นที่พูดถึง "cars and trucks" แต่การค้นหาเชิงความหมายจะเข้าใจว่าเหมือนกันและคืนผลลัพธ์ที่เกี่ยวข้องครบถ้วน:

<img src="../../../translated_images/th/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*แผนภาพนี้เทียบการค้นหาด้วยคำหลักกับการค้นหาเชิงความหมาย แสดงให้เห็นว่าการค้นหาเชิงความหมายดึงข้อมูลที่เกี่ยวข้องถึงแนวคิดแม้ว่าคำหลักจะต่างกัน*
ภายในระบบ ความคล้ายคลึงถูกวัดโดยใช้ cosine similarity — โดยถามว่า "ลูกศรสองอันนี้ชี้ไปในทิศทางเดียวกันหรือไม่?" สองส่วนของข้อความอาจใช้คำต่างกันโดยสิ้นเชิง แต่ถ้าหมายความเหมือนกัน เวกเตอร์ของพวกมันจะชี้ไปในทิศทางเดียวกันและคะแนนจะใกล้เคียงกับ 1.0:

<img src="../../../translated_images/th/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*ไดอะแกรมนี้แสดง cosine similarity เป็นมุมระหว่างเวกเตอร์ embedding — เวกเตอร์ที่เรียงตัวกันมากกว่าจะได้คะแนนใกล้เคียง 1.0 ซึ่งบ่งชี้ความคล้ายคลึงทางความหมายสูง*

> **🤖 ลองกับ [GitHub Copilot](https://github.com/features/copilot) Chat:** เปิดไฟล์ [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) แล้วถาม:
> - "การค้นหาความคล้ายคลึงทำงานอย่างไรกับ embeddings และอะไรเป็นตัวกำหนดคะแนน?"
> - "ควรใช้เกณฑ์ความคล้ายคลึงเท่าไหร่และมีผลอย่างไรต่อผลลัพธ์?"
> - "จัดการกรณีที่ไม่พบเอกสารที่เกี่ยวข้องอย่างไร?"

### การสร้างคำตอบ

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

ส่วนที่เกี่ยวข้องมากที่สุดจะถูกจัดรวมเข้าเป็น prompt ที่มีโครงสร้างโดยรวมคำสั่งที่ชัดเจน ข้อมูลบริบทที่ถูกดึงมา และคำถามของผู้ใช้ โมเดลจะอ่านแต่ส่วนเหล่านี้และตอบคำถามโดยอิงจากข้อมูลเหล่านั้น — ตัวโมเดลจะใช้ได้เฉพาะข้อมูลที่ปรากฏอยู่ตรงหน้าเท่านั้น ซึ่งช่วยป้องกันการคาดเดาผิดๆ

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

ไดอะแกรมข้างล่างแสดงการจัดรวมนี้แบบแอคทีฟ — ส่วนที่ได้คะแนนสูงสุดจากขั้นตอนค้นหาจะถูกใส่เข้าไปในแม่แบบ prompt และ `OpenAiOfficialChatModel` จะสร้างคำตอบที่มีข้อมูลรองรับ:

<img src="../../../translated_images/th/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*ไดอะแกรมนี้แสดงการจัดรวมส่วนที่ได้คะแนนสูงสุดเข้าไปใน prompt ที่มีโครงสร้าง อนุญาตให้โมเดลสร้างคำตอบที่มีข้อมูลรองรับจากข้อมูลของคุณ*

## การรันแอปพลิเคชัน

**ตรวจสอบการดีพลอย:**

ตรวจสอบให้แน่ใจว่าไฟล์ `.env` มีอยู่ที่โฟลเดอร์หลักพร้อมกับข้อมูลประจำตัว Azure (สร้างขึ้นใน Module 01) รันคำสั่งนี้จากโฟลเดอร์โมดูล (`03-rag/`):

**Bash:**
```bash
cat ../.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ควรแสดง AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**เริ่มแอปพลิเคชัน:**

> **หมายเหตุ:** หากคุณเริ่มแอปทั้งหมดแล้วโดยใช้ `./start-all.sh` จากโฟลเดอร์หลัก (ตามที่อธิบายไว้ใน Module 01) โมดูลนี้จะทำงานอยู่บนพอร์ต 8081 แล้ว คุณสามารถข้ามคำสั่งเริ่มด้านล่างและเข้าไปที่ http://localhost:8081 ได้เลย

**ตัวเลือกที่ 1: ใช้ Spring Boot Dashboard (แนะนำสำหรับผู้ใช้ VS Code)**

ภายใน dev container มีส่วนขยาย Spring Boot Dashboard ที่ให้หน้าต่างแบบกราฟิกสำหรับจัดการแอป Spring Boot ทั้งหมด คุณจะเห็นไอคอนนี้ในแถบกิจกรรมด้านซ้ายของ VS Code (มองหาไอคอน Spring Boot)

จาก Spring Boot Dashboard คุณสามารถ:
- ดูแอป Spring Boot ทั้งหมดใน workspace
- เริ่ม/หยุดแอปด้วยคลิกเดียว
- ดูบันทึกแอปแบบเรียลไทม์
- ตรวจสอบสถานะแอป

เพียงคลิกปุ่มเล่นข้าง "rag" เพื่อเริ่มโมดูลนี้ หรือเริ่มโมดูลทั้งหมดพร้อมกัน

<img src="../../../translated_images/th/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*ภาพหน้าจอนี้แสดง Spring Boot Dashboard ใน VS Code ที่ให้คุณเริ่ม หยุด และตรวจสอบแอปแบบกราฟิก*

**ตัวเลือกที่ 2: ใช้สคริปต์เชลล์**

เริ่มเว็บแอปทั้งหมด (โมดูล 01-04):

**Bash:**
```bash
cd ..  # จากไดเรกทอรีราก
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # จากไดเรกทอรีรูท
.\start-all.ps1
```

หรือเริ่มแค่โมดูลนี้:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

ทั้งสองสคริปต์จะโหลด environment variables จากไฟล์ `.env` ที่โฟลเดอร์หลักโดยอัตโนมัติ และจะสร้างไฟล์ JAR ถ้ายังไม่มี

> **หมายเหตุ:** ถ้าคุณต้องการสร้างโมดูลทั้งหมดด้วยตัวเองก่อนเริ่มรัน:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

เปิด http://localhost:8081 ในเบราว์เซอร์ของคุณ

**เพื่อหยุด:**

**Bash:**
```bash
./stop.sh  # เฉพาะโมดูลนี้
# หรือ
cd .. && ./stop-all.sh  # ทุกโมดูล
```

**PowerShell:**
```powershell
.\stop.ps1  # โมดูลนี้เท่านั้น
# หรือ
cd ..; .\stop-all.ps1  # ทุกโมดูล
```

## การใช้แอปพลิเคชัน

แอปนี้ให้หน้าเว็บสำหรับอัพโหลดเอกสารและถามคำถาม

<a href="images/rag-homepage.png"><img src="../../../translated_images/th/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*ภาพหน้าจอนี้แสดงอินเทอร์เฟซแอป RAG ที่คุณอัพโหลดเอกสารและถามคำถามได้*

### การอัพโหลดเอกสาร

เริ่มจากการอัพโหลดเอกสาร — ไฟล์ TXT เหมาะสำหรับการทดสอบที่สุด มีไฟล์ตัวอย่าง `sample-document.txt` ในโฟลเดอร์นี้ที่มีข้อมูลเกี่ยวกับฟีเจอร์ LangChain4j การใช้งาน RAG และแนวทางปฏิบัติที่ดีที่สุด — เหมาะสำหรับการทดสอบระบบ

ระบบจะประมวลผลเอกสารของคุณ แบ่งเป็นชิ้นส่วน และสร้าง embeddings สำหรับแต่ละชิ้น ซึ่งเกิดขึ้นโดยอัตโนมัติเมื่อคุณอัพโหลด

### ถามคำถาม

ตอนนี้ถามคำถามเฉพาะเจาะจงเกี่ยวกับเนื้อหาในเอกสาร ลองถามสิ่งที่เป็นข้อเท็จจริงที่ชัดเจนในเอกสาร ระบบจะค้นหาชิ้นส่วนที่เกี่ยวข้อง นำมารวมใน prompt และสร้างคำตอบขึ้น

### ตรวจสอบแหล่งที่มา

สังเกตว่าแต่ละคำตอบจะมีการอ้างอิงแหล่งที่มาพร้อมคะแนนความคล้ายคลึง คะแนนเหล่านี้ (0 ถึง 1) แสดงว่าชิ้นส่วนแต่ละอันเกี่ยวข้องกับคำถามของคุณเพียงใด ยิ่งคะแนนสูงหมายถึงแมตช์ที่ดียิ่งขึ้น ทำให้คุณตรวจสอบคำตอบกับแหล่งข้อมูลได้

<a href="images/rag-query-results.png"><img src="../../../translated_images/th/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*ภาพหน้าจอนี้แสดงผลลัพธ์คำถามพร้อมคำตอบที่สร้างขึ้น การอ้างอิงแหล่งที่มา และคะแนนความเกี่ยวข้องสำหรับแต่ละชิ้นส่วนที่ถูกดึงมา*

### ทดลองถามคำถาม

ลองตั้งคำถามหลายๆ แบบ:
- ข้อเท็จจริงเฉพาะ: "หัวข้อหลักคืออะไร?"
- การเปรียบเทียบ: "ความแตกต่างระหว่าง X กับ Y คืออะไร?"
- สรุป: "สรุปประเด็นสำคัญเกี่ยวกับ Z"

ดูว่าคะแนนความเกี่ยวข้องเปลี่ยนแปลงอย่างไรขึ้นกับว่าคำถามของคุณตรงกับเนื้อหาเอกสารเพียงใด

## แนวคิดสำคัญ

### กลยุทธ์การแบ่งชิ้น

เอกสารถูกแบ่งเป็นชิ้นละ 300 โทเค็นโดยทับซ้อนกัน 30 โทเค็น สมดุลนี้ช่วยให้แต่ละชิ้นมีบริบทเพียงพอที่จะมีความหมายในขณะเดียวกันก็ยังเล็กพอที่จะรวมหลายชิ้นใน prompt ได้

### คะแนนความคล้ายคลึง

ชิ้นส่วนที่ถูกดึงมาทั้งหมดมาพร้อมคะแนนความคล้ายคลึงจาก 0 ถึง 1 แสดงว่าตรงกับคำถามของผู้ใช้มากน้อยแค่ไหน ไดอะแกรมด้านล่างแสดงช่วงคะแนนและวิธีการใช้คะแนนเหล่านี้เพื่อกรองผลลัพธ์:

<img src="../../../translated_images/th/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*ไดอะแกรมนี้แสดงช่วงคะแนนจาก 0 ถึง 1 โดยมีเกณฑ์ขั้นต่ำที่ 0.5 สำหรับคัดกรองชิ้นส่วนที่ไม่เกี่ยวข้อง*

คะแนนแบ่งเป็น:
- 0.7-1.0: เกี่ยวข้องสูง ตรงกันเป๊ะ
- 0.5-0.7: เกี่ยวข้อง มีบริบทดี
- ต่ำกว่า 0.5: ถูกคัดกรองออก ไม่เกี่ยวข้อง

ระบบจะดึงแต่ชิ้นที่มีคะแนนสูงกว่าเกณฑ์ขั้นต่ำเพื่อให้มั่นใจในคุณภาพ

Embeddings ทำงานได้ดีเมื่อความหมายกระจุกตัวได้ชัดเจน แต่ก็มีจุดอ่อน ไดอะแกรมด้านล่างแสดงรูปแบบความล้มเหลวทั่วไป — ชิ้นใหญ่เกินไปทำให้เวกเตอร์มัว ชิ้นเล็กเกินไม่มีบริบท คำที่กำกวมชี้ไปหลายกลุ่ม และการค้นหาตรงตัว (เช่น รหัส ID, หมายเลขชิ้นส่วน) ใช้ embeddings ไม่ได้เลย:

<img src="../../../translated_images/th/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*ไดอะแกรมนี้แสดงรูปแบบความล้มเหลวของ embedding ทั่วไป: ชิ้นใหญ่เกินไป ชิ้นเล็กเกินไป คำกำกวมที่ชี้หลายกลุ่ม และการค้นหาตรงตัวเช่น ID*

### การเก็บในหน่วยความจำ

โมดูลนี้ใช้การเก็บข้อมูลในหน่วยความจำเพื่อความง่าย เมื่อคุณรีสตาร์ทแอป เอกสารที่อัพโหลดจะหายไป ระบบจริงมักใช้ฐานข้อมูลแบบเวกเตอร์ที่เก็บถาวร เช่น Qdrant หรือ Azure AI Search

### การจัดการบริบทในหน้าต่าง

แต่ละโมเดลมีขอบเขตบริบทสูงสุด คุณไม่สามารถใส่ชิ้นทุกชิ้นจากเอกสารใหญ่ๆ ได้ ระบบจะดึงชิ้นที่เกี่ยวข้องสูงสุด N ชิ้น (ค่าสำเร็จรูปคือ 5) เพื่อความเหมาะสมกับขอบเขตและยังมีบริบทเพียงพอสำหรับคำตอบที่แม่นยำ

## เมื่อไหร่ที่ RAG สำคัญ

RAG ไม่ใช่วิธีที่เหมาะสมเสมอไป ไกด์การตัดสินใจด้านล่างช่วยคุณประเมินว่าเมื่อไหร่ RAG จะเพิ่มมูลค่า และเมื่อไหร่ควรใช้วิธีที่ง่ายกว่า — เช่น การใส่เนื้อหาลงใน prompt โดยตรง หรือพึ่งพาความรู้ในตัวโมเดล:

<img src="../../../translated_images/th/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*ไดอะแกรมนี้แสดงไกด์การตัดสินใจว่าเมื่อไหร่ RAG จะเพิ่มมูลค่าและเมื่อไหร่ควรใช้วิธีที่ง่ายกว่า*

## ขั้นตอนต่อไป

**โมดูลถัดไป:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**การนำทาง:** [← ก่อนหน้า: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [กลับสู่หน้าแรก](../README.md) | [ถัดไป: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าพวกเราจะพยายามให้มีความถูกต้อง แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ ในกรณีข้อมูลที่สำคัญ แนะนำให้ใช้บริการแปลโดยมนุษย์ผู้เชี่ยวชาญ พวกเราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดใด ๆ ที่อาจเกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->