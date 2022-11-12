package com.example.pictureApi.repository;

import com.example.pictureApi.Model.PictureInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PictureRepository extends JpaRepository<PictureInfo, Long> {
}
