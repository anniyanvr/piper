FROM       jrottenberg/ffmpeg:4.2-ubuntu

# Install Java etc.
RUN        apt-get update && \
           apt-get -y install openjdk-8-jre python mediainfo wget unzip
           
WORKDIR    /app

# Install Bento4 (https://www.bento4.com/)
RUN        wget "http://zebulon.bok.net/Bento4/binaries/Bento4-SDK-1-5-1-629.x86_64-unknown-linux.zip" && \
           unzip Bento4-SDK-1-5-1-629.x86_64-unknown-linux.zip && \
           mv Bento4-SDK-1-5-1-629.x86_64-unknown-linux bento4 && \
           rm Bento4-SDK-1-5-1-629.x86_64-unknown-linux.zip
           
ENV        PATH $PATH:/app/bento4/bin

ENTRYPOINT []

ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

CMD ["java","-cp","/app/lib/*:/app","com.creactiviti.piper.PiperApplication"]