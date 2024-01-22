## 2024.01.15
1. [설계] 요구사항 명세서 및 기능 명세서를 작성하였습니다.

![](https://github.com/BoyCho/ProblemSolving/assets/53038672/4c3d0703-c7e7-474a-86f2-43568c0edb2b)


<br>

2. [설계] 대략적인 흐름을 이해하기위해 구조를 구성해보았습니다.

![](https://github.com/BoyCho/ProblemSolving/assets/53038672/7b38fa19-c0d1-421d-b605-f81946d533f5)

<br>

## 2024.01.16
밑그림을 그려야 기능 명세 및 역할 분배를 할 수 있다고 생각하여 필요 기술을 간략히 알아보았습니다. <br>
차후 진행 및 타 프로젝트 진행 시에는 **기능, API 명세**를 우선하여 틀을 잡고 진행하겠습니다.
 > 인코딩

    - 표준 인코딩 활용
    - 어떤 서버에서 하는가? GPU? EC2? => EC2 혹은 별도 서버에서 진행 예정
    - 꼭 필요한가? => 현재는 최대한 S3의 Presigned URL을 활용하여 서버 간 영상 전송 예정

 > 동영상 저장
    
    - 원본 동영상 및 AI 처리 결과 동영상은 S3에 저장 후, 해당 URL을 DB에 저장 예정
      => 보안 이슈 확인 필요
 
 > 사용자에게 영상 제공

    - S3의 FrontCloud 사용
      => S3 - AWS Elastic Transcoder - S3 - FrontCloud 순서로 사용자에게 스트리밍 서비스 제공
    - 우선은 URL을 제공하고, 차후 정확한 기술 이해에 따라 스트리밍을 제공 여부 판단 예정

<br>

추가로 작성된 요구사항 명세서를 바탕으로, Backend파트의 기능 명세서를 작성중에 있습니다.

<br>

## 2024.01.17
```
- 기능 명세서 작성 후 피드백을 받아 프로그램 명세서 작성 중에 있습니다.
- 사용자 요청에 따른 백엔드 단에서의 흐름을 나누고, 흐름마다의 시스템 구상을 나누었습니다.
- 시스템 구상을 자세히 명세한 후 팀원별 역할 분배 및 구현 단계 설정을 용이하게 할 계획입니다.
```

## 2024.01.18
```
- 사용자 요청에 따른 백엔드 처리 흐름을 정리하였습니다.
- 흐름을 토대로 구현해야 할 기능 리스트업과 일정을 분배 중입니다.
```

## 2024.01.19
```
- 구상 흐름에 맞춰 프로그램 명세서를 리스트업 하였습니다.
- 피드백에 맞춰 전체 기능에 대해 리스트업 하였습니다.
```