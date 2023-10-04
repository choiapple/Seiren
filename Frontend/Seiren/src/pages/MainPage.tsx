import { useRef } from "react";
import styles from "./MainPage.module.css";
import { Link as ScrollLink } from "react-scroll"
import AboutPage from "./AboutPage";
import AboutImg from "../assets/img/about.png";

import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

gsap.registerPlugin(ScrollTrigger);

function MainPage() {
  const imgRef = useRef<HTMLImageElement>(null);
  const handleImageLoad = () => {
    gsap.to(imgRef.current, {
      rotation: "-=20",
      yoyo: true,
      repeat: -1,
      duration: 2,
      ease: "power1.inOut"
    });
  };

  return (
    <div className={styles.container}>
      <section className={styles.section}>
        <div className={styles.main}>
        <div className={styles.main_txt}>From <span>Recording</span> your voice to <span>Selling</span> yours <br/> all at once</div>
          <ScrollLink to='about' smooth={true} duration={500}>
            <div className={styles.go_about}>
              <div className={styles.go_about_txt}> ABOUT</div>  
            </div>
          </ScrollLink>
        <div className={styles.main_img}><img ref={imgRef} src={AboutImg} alt="img" onLoad={handleImageLoad} /></div>   
        </div>
        <hr className={styles.hr} />
      </section>

      <div id="about">
        <AboutPage />
      </div>
    </div>
  );
}

export default MainPage;