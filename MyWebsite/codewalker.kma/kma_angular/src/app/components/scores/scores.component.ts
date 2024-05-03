import {Component, Input, OnInit} from '@angular/core';
import {ScoreService} from "../services/score.service";
import {ScoreDto} from "../dtos/score.dto";
import {StudentDto} from "../dtos/student.dto";
import {error} from "@angular/compiler-cli/src/transformers/util";
import * as ts from 'typescript';
import {ScoreResponse} from "../responses/score.response";
import {RankingDto} from "../dtos/ranking.dto";
import {RankingService} from "../services/ranking.service";
import {SearchService} from "../services/search.service";
import {style} from "@angular/animations";

@Component({
  selector: 'app-scores',
  templateUrl: './scores.component.html',
  styleUrls: ['./scores.component.scss', './fonts/icomoon/style.css', './css/style.css', './css/owl.carousel.min.css',
  './css/bootstrap.min.css']
})
export class ScoresComponent implements OnInit{
  scores?: ScoreDto[];
  student_code: string = "";
  id: number = 0;
  hitButton: boolean = false;
  searched: StudentDto[] = [];
  topRanking: RankingDto[] = [];
  subjectsFailed: number = 0;
  cloneRanking: RankingDto = {
    student_name: '',
    student_code: '',
    student_class: '',
    ranking: 0,
    gpa: 0,
    asia_gpa: 0
  }
  student: StudentDto = {
    student_code: "",
    student_class: "",
    student_name: ""
  }
  ranking: RankingDto = {
    student_name: '',
    student_code: '',
    student_class: '',
    ranking: 0,
    gpa: 0,
    asia_gpa: 0
  }
  constructor(private scoreService: ScoreService,
              private rankingService: RankingService,
              private searchService: SearchService) {
  }

  ngOnInit() {
    debugger;
    this.topRanking=[];
    this.rankingService.getRankingTop2().subscribe({
      next: (responses: any) =>{
        this.cloneRanking = responses;
        this.topRanking.push(this.cloneRanking);
      }
    })
    this.rankingService.getRankingTop1().subscribe({
      next: (response: any) =>{
        this.cloneRanking = response;
        this.topRanking.push(this.cloneRanking);
      }
    })
    this.rankingService.getRankingTop3().subscribe({
      next: (response: any) =>{
        this.cloneRanking = response;
        this.topRanking.push(this.cloneRanking);
      }
    })
    debugger;

    this.searchService.searchButtonClick$.subscribe(() => {
      this.hitButton=true;
    });
    // localStorage.removeItem('searchHistory');
    // Lấy dữ liệu từ localStorage
    const searchHistory = localStorage.getItem('searchHistory');

    if (searchHistory) {
      // Chuyển đổi chuỗi JSON thành mảng đối tượng StudentDTO
      this.searched = JSON.parse(searchHistory);
      debugger;
    } else {
      const searched: StudentDto[] = [];
    }

  }
  fetchData(){
    this.subjectsFailed =0;
    this.scores = [];
    debugger;
    this.id = 0;
    console.log("Đã bấm nút search");
    this.scoreService.getScoresByStudentCode(this.student_code).subscribe({
      next: (response: any) => {
        debugger;
        this.student = response.student_response;
        // if (localStorage.getItem('searchHistory')?.valueOf())
        // localStorage.setItem("searchHistory", localStorage.getItem('searchHistory')?.valueOf()+this.student)
        let searchHistory = JSON.parse(localStorage.getItem('searchHistory') || '[]');

// Kiểm tra xem student_code đã tồn tại trong searchHistory hay không
        let existingStudent = searchHistory.find((student: any) => student.student_code === this.student.student_code);

// Nếu student_code chưa tồn tại, thêm phần tử mới vào
        if (!existingStudent) {
          searchHistory.unshift({
            student_class: this.student.student_class,
            student_code: this.student.student_code,
            student_name: this.student.student_name
          });

          localStorage.setItem('searchHistory', JSON.stringify(searchHistory));
        }

        response.scores_response.forEach((scoreItem: ScoreResponse) => {
          const score: ScoreDto = {
            score_over_rall: scoreItem.score_over_rall,
            score_final: scoreItem.score_final,
            score_second: scoreItem.score_second,
            score_first: scoreItem.score_first,
            score_text: scoreItem.score_text,
            subject_name: scoreItem.subject_name
          };
          debugger;
          if (this.scores) this.scores.push(score);
        });
        this.updateFailedSubjects();
        debugger;
        this.rankingService.getRanking(this.student_code).subscribe({
          next: (response: any) =>{
            debugger;
            this.ranking = response;
          },
          complete: () =>{
            debugger;
            this.hitButton = false;
          },
          error: (error: any) =>{
            debugger;
            console.log("Error fetching data "+error.error.message);
          }
        })
      },
      complete: () =>{
        debugger;
        this.hitButton = false;
      },
      error: (error: any) =>{
        debugger;
        console.log("Error fetching data: "+error.error.message);
      }
    })
    console.log(this.student.student_name+"\n"+this.scores?.toString());
  }
  onSubmit() {
    this.fetchData();
  }
  updateFailedSubjects() {
    // Tính toán số môn học trượt ở đây
    this.subjectsFailed = 0;
    if (this.scores)
    for (let score of this.scores) {
      if (score.score_text === 'F') {
        this.subjectsFailed++;
      }
    }
  }
  patchValue(id: number){
    this.id = id;
  }
  updateStudentCode(studentCode: string){
    this.student_code = studentCode;
    this.fetchData();
  }

  protected readonly style = style;
}
