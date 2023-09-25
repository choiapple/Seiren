import { useEffect } from "react";
import { useRecoilState } from "recoil";
import { myVoiceState } from "../../recoil/UserAtom";
import { customAxios } from "../../libs/axios";
import styles from "./YourVoice.module.css";

function YourVoice() {
  const [myVoice, setMyVoice] = useRecoilState(myVoiceState);

  useEffect(() => {
    customAxios
      .get("voices")
      .then(response => {
        const responseData = response.data.response;
        console.log("yourVoice : ", responseData);
        setMyVoice(responseData);
      })
      .catch(error => {
        console.log("내 목소리 API 호출 중 오류 발생:", error);
      });
  }, []);
  console.log(myVoice);
  return (
    <div className={styles.YourVoiceContainer}>
      <div className={styles.YourVoiceText}>Your Voice</div>
      <div className={styles.VoiceItems}>
        <ul>
          {myVoice.map(item => (
            <li key={item.voiceId}>
              {item.voiceTitle}
              <img className={styles.pimg} src={item.voiceAvatarUrl} alt={item.title} />
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default YourVoice;