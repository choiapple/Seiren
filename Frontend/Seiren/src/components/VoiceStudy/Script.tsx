import { useRecoilState } from 'recoil';
import { RecordState } from '../../recoil/RecordAtom';
import styles from "./Script.module.css"

interface ScriptProps {
  index: number; // 현재 script의 인덱스 값을 받는 props 추가
  setIndex: React.Dispatch<React.SetStateAction<number>>; // index 변경 함수를 받는 props 추가
}

const Script: React.FC<ScriptProps> = ({ index, setIndex }) => {
  // 더미 데이터 생성
  const scripts = Array.from({length: 10}, (_, i) => `스크립트 문장 ${i+1}`);

  const [recordingStatus, setRecordingStatus] = useRecoilState(RecordState);

  const goNext = (): void => {
    if (index < scripts.length-1) 
      setIndex(index+1);
      if(recordingStatus === "stopped"){
        console.log("녹음이 완료되었습니다. 다음으로 넘어갑니다.")
        setRecordingStatus("idle");
      }
  }

  return (
    <div>      
      <div className={styles.text}>
        <div className={styles.text_now}>{scripts[index]}</div>
        <div className={styles.text_next}>NEXT {scripts[index + 1]}</div>
      </div>
        
      <hr className={styles.hr} />

      <div className={styles.btn}>
        <div className={styles.record2} onClick={()=>{
          console.log("다시 녹음 버튼 클릭");
          setRecordingStatus("idle");
        }}>다시 녹음</div>
        <div className={styles.next} onClick={goNext}>다음</div>
      </div>
    </div>
  );
};

export default Script;